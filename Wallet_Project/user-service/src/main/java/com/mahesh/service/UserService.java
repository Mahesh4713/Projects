package com.mahesh.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahesh.model.User;
import com.mahesh.repository.UserCacheRepository;
import com.mahesh.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private static final String USER_CREATE_TOPIC = "user_created";

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCacheRepository userCacheRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    private ObjectMapper objectMapper=new ObjectMapper();

    public void create(User user) throws JsonProcessingException {
        userRepository.save(user);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("Phone",user.getPhone());
        jsonObject.put("Email",user.getEmail());

        kafkaTemplate.send(USER_CREATE_TOPIC,this.objectMapper.writeValueAsString(jsonObject));

    }


    public User get(Integer userId) throws Exception {
        User user=userCacheRepository.get(userId);
                if(user !=null){
                    return user;
                }
                user=userRepository.findById(userId).orElseThrow(Exception::new);
                userCacheRepository.set(user);
                return user;
    }
}
