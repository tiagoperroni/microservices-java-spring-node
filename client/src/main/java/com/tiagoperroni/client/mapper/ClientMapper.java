package com.tiagoperroni.client.mapper;

import com.tiagoperroni.client.model.AdressResponse;
import com.tiagoperroni.client.model.ClientRequest;
import com.tiagoperroni.client.model.ClientResponse;

public class ClientMapper {

    public static ClientResponse convert(ClientRequest request, AdressResponse adressResponse) {
        var clientResponse = new ClientResponse();
        clientResponse.setName(request.getName());
        clientResponse.setCpf(request.getCpf());
        clientResponse.setIsActive(true);
        clientResponse.setAdress(adressResponse);       
        return clientResponse;
        
    }
    
}
