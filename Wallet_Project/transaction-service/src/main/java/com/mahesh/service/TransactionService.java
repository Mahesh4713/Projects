package com.mahesh.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahesh.configs.KafkaConfig;
import com.mahesh.dto.TransactionCreateRequest;
import com.mahesh.models.Transaction;
import com.mahesh.models.TransactionStatus;
import com.mahesh.repository.TransactionRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    private ObjectMapper objectMapper=new ObjectMapper();

    private static final String TRANSACTION_CREATED_TOPIC = "transaction_created";
    private static final String WALLET_UPDATED_TOPIC = "wallet_updated";

    private static final String WALLET_UPDATE_SUCCESS_STATUS = "SUCCESS";
    private static final String WALLET_UPDATE_FAILED_STATUS = "FAILED";


    public String transact(TransactionCreateRequest request) throws JsonProcessingException {

        Transaction transaction=Transaction.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .externalId(UUID.randomUUID().toString())
                .transactionStatus(TransactionStatus.PENDING)
                .amount(request.getAmount())
                .reason(request.getReason())
                .build();
        transactionRepository.save(transaction);

        JSONObject obj=new JSONObject();
        obj.put("senderId", transaction.getSenderId());
        obj.put("receiverId", transaction.getReceiverId());
        obj.put("amount", transaction.getAmount());
        obj.put("transactionId", transaction.getExternalId());
        kafkaTemplate.send(TRANSACTION_CREATED_TOPIC, objectMapper.writeValueAsString(obj));


        return transaction.getExternalId();
    }

    @KafkaListener(topics = WALLET_UPDATED_TOPIC,groupId = "demowalletproject")
    public void updateTransaction(String msg) throws ParseException {
        System.out.println("testing " + msg);
        JSONObject obj=(JSONObject) new JSONParser().parse(msg);
        String externalTransactionId= (String) obj.get("transactionId");
        String senderPhoneNumber= (String) obj.get("senderWalletId");
        String receiverPhoneNumber= (String) obj.get("receiverWalletId");
        String walletUpdateStatus= (String) obj.get("status");

        TransactionStatus transactionStatus;

        if(walletUpdateStatus.equals(WALLET_UPDATE_FAILED_STATUS)){
            transactionStatus = TransactionStatus.FAILED;
            transactionRepository.updateTransaction(externalTransactionId,transactionStatus);
        }else{
            transactionStatus = TransactionStatus.SUCCESSFUL;
            transactionRepository.updateTransaction(externalTransactionId, transactionStatus);
        }

    }
}
