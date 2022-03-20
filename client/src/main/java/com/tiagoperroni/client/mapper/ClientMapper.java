package com.tiagoperroni.client.mapper;

import com.tiagoperroni.client.exceptions.PasswordNotMatchException;
import com.tiagoperroni.client.model.AdressResponse;
import com.tiagoperroni.client.model.Client;
import com.tiagoperroni.client.model.ClientRequest;
import com.tiagoperroni.client.model.ClientResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ClientMapper {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static Client convertFromClientRequest(ClientRequest request, AdressResponse adressResponse) {
        var clientResponse = new Client();
        clientResponse.setName(request.getName());
        clientResponse.setCpf(request.getCpf());
        clientResponse.setEmail(request.getEmail());
        verifyPassword(request);
        clientResponse.setPassword(passwordEncoder.encode(request.getPassword()));
        clientResponse.setIsActive(true);
        clientResponse.setAdress(adressResponse);       
        return clientResponse;
        
    }

    public static void verifyPassword(ClientRequest request) {
        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new PasswordNotMatchException("The password don´t match, try again.");
        }
    }

    public static ClientResponse convertFromClient(Client request) {
        var clientResponse = new ClientResponse();
        clientResponse.setId(request.getId());
        clientResponse.setName(request.getName());
        clientResponse.setCpf(request.getCpf());
        clientResponse.setEmail(request.getEmail());      
        clientResponse.setPassword(request.getPassword());
        clientResponse.setIsActive(true);
        clientResponse.setAdress(request.getAdress());       
        return clientResponse;
        
    }

    public static Client convertFromClientResponse(ClientResponse request) {
        var client = new Client();
        client.setName(request.getName());
        client.setCpf(request.getCpf());
        client.setEmail(request.getEmail());      
        client.setPassword(request.getPassword());
        client.setIsActive(true);
        client.setAdress(request.getAdress());       
        return client;
        
    }


    
}
