package com.tiagoperroni.client.model;

import lombok.Data;

@Data
public class ClientResponseLogin {

    private String cpf;
    private String email;
    private String password;
    
}
