package com.tiagoperroni.order.service;

import java.util.UUID;

import com.tiagoperroni.order.feign.ClientFeignRequest;
import com.tiagoperroni.order.model.ClientLoginCpf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoginService {

    @Autowired
    private ClientFeignRequest feignRequest;

    public String token = null;

    public String clientLogin(ClientLoginCpf cpf) {
        var response = this.feignRequest.clientLogin(cpf).getBody();
        if (response != null) {
            this.token = UUID.randomUUID().toString();
            return token;
        }
        return null;        
    }
    
}
