package com.tiagoperroni.client.model;

import lombok.Data;

@Data
public class ClientResponse {

    private Integer id;
    private String name;
    private String cpf;
    private Boolean isActive;
    private String clientPort; 

    private AdressResponse adress;
    
}
