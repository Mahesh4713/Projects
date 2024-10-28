package com.mahesh.service;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahesh.model.Wallet;
import com.mahesh.repository.WalletRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService {


    @Autowired
    WalletRepository walletRepository;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();


    private static final String USER_CREATE_TOPIC = "user_created";
    private static final String TRANSACTION_CREATED_TOPIC = "transaction_created";
    private static final String WALLET_UPDATED_TOPIC = "wallet_updated";

    @Value("${wallet.initial.balance}")
    long balance;


//User onboarding flow

    @KafkaListener(topics = USER_CREATE_TOPIC,groupId = "demowalletproject")
    public void createWallet(String msg) throws ParseException {
        System.out.println("testing " + msg);
        JSONObject jsonObject=(JSONObject) new JSONParser().parse(msg);
        String walletId= (String) jsonObject.get("Phone");

        Wallet wallet=Wallet.builder()
                .walletId(walletId)
                .currency("INR")
                .balance(balance)
                .build();
        walletRepository.save(wallet);
    }
//
@KafkaListener(topics = TRANSACTION_CREATED_TOPIC,groupId = "demowalletproject")
public void updateWallets(String msg) throws ParseException, JsonProcessingException {
    System.out.println("Wallet testing " + msg);
    JSONObject obj = (JSONObject) new JSONParser().parse(msg);
    String senderWalletId = (String) obj.get("senderId");
    String receiverWalletId = (String) obj.get("receiverId");
    long amount = (long) obj.get("amount");
    String transactionId = (String) obj.get("transactionId");

    try {
        Wallet senderWallet = walletRepository.findByWalletId(senderWalletId);
        Wallet receiverWallet = walletRepository.findByWalletId(receiverWalletId);


        if (senderWallet == null || receiverWallet == null || senderWallet.getBalance() < amount) {
            obj = this.init(senderWalletId, receiverWalletId, transactionId, amount, "FAILED");
            obj.put("senderWalletBalance", senderWallet == null ? 0 : senderWallet.getBalance());

//            obj =new JSONObject();
//            obj.put("transactionId",transactionId);
//            obj.put("status","FAILED");
//            obj.put("senderWalletId",senderWalletId);
//            obj.put("receiverWalletId",receiverWalletId);
//            obj.put("senderWalletBalance",senderWallet==null ?0.:senderWallet.getWalletId());

            kafkaTemplate.send(WALLET_UPDATED_TOPIC, this.objectMapper.writeValueAsString(obj));
            System.out.println("TESTING TRANSACTION"+ obj);
            return;
        }

        walletRepository.updateWallet(senderWalletId, -amount);
        walletRepository.updateWallet(receiverWalletId, amount);

        obj = this.init(senderWalletId, receiverWalletId, transactionId, amount, "SUCCESS");

//        obj =new JSONObject();
//        obj.put("transactionId",transactionId);
//        obj.put("status","SUCCESS");
//        obj.put("senderWalletId",senderWalletId);
//        obj.put("receiverWalletId",receiverWalletId);
//        obj.put("amount",amount);
        kafkaTemplate.send(WALLET_UPDATED_TOPIC, this.objectMapper.writeValueAsString(obj));


    } catch (Exception e) {
        obj = this.init(senderWalletId, receiverWalletId, transactionId, amount, "FAILED");
//        obj.put("transactionId",transactionId);
//        obj.put("senderWalletId",senderWalletId);
//        obj.put("receiverWalletId",receiverWalletId);
//        obj.put("status","FAILED");
        obj.put("ErrorMsg", e.getMessage());
        kafkaTemplate.send(WALLET_UPDATED_TOPIC, this.objectMapper.writeValueAsString(obj));

    }
    //TODO produce event of wallet creation

}

    public JSONObject init(String senderId,String receiverId,String transactionId,Long amount,String status){
        JSONObject obj=new JSONObject();
        obj.put("senderWalletId",senderId);
        obj.put("receiverWalletId",receiverId);
        obj.put("transactionId",transactionId);
        obj.put("amount",amount);
        obj.put("status",status);
        return obj;
    }
//    [GenericMessage [payload={"Email":"sarada@gmail.com","Phone":"1111111"}, headers={kafka_offset=6, kafka_consumer=org.springframework.kafka.core.DefaultKafkaConsumerFactory$ExtendedKafkaCo


}
