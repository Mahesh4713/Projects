package com.mahesh.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    SimpleMailMessage getMailMessage(){
        return new SimpleMailMessage();
    }
    @Bean
    JavaMailSenderImpl getMailSender(){
        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("maheshwalletproject@gmail.com");
        javaMailSender.setPassword("tjke ynrd gsqn rteu");

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.debug", true);

        properties.put("mail.smtp.starttls.enable", true);

        return javaMailSender;

    }


}
