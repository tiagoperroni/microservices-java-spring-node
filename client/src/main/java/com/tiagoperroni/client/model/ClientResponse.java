package com.tiagoperroni.client.model;

import lombok.Data;

@Data
public class ClientResponse {
  
    private Integer id;
    private String name;
    private String cpf;
    private String email;
    private String password;  
    private Boolean isActive;
    private String clientPort;    

    private AdressResponse adress;
    
}
