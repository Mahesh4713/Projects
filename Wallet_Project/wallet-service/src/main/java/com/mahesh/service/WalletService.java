package com.mahesh.service;


import com.fasterxml.jackson.core.JsonParser;
import com.mahesh.model.Wallet;
import com.mahesh.repository.WalletRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WalletService {


    @Autowired
    WalletRepository walletRepository;

    private static final String USER_CREATE_TOPIC = "user_created";

    @Value("${wallet.initial.balance}")
    long balance;


    @KafkaListener(topics = USER_CREATE_TOPIC,groupId = "walletproject")
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
//    [GenericMessage [payload={"Email":"sarada@gmail.com","Phone":"1111111"}, headers={kafka_offset=6, kafka_consumer=org.springframework.kafka.core.DefaultKafkaConsumerFactory$ExtendedKafkaCo

    //TODO produce event of wallet creation
}
