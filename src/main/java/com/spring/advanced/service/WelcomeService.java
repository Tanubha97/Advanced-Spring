package com.spring.advanced.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WelcomeService {

    @Value("${welcome.message}")
    private String welcomeMessage;

    public  String retrieveMessage(){
        return  welcomeMessage;
    }
}
