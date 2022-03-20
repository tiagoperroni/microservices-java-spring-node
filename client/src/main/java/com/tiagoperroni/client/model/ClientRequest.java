package com.tiagoperroni.client.model;

import lombok.Data;

@Data
public class ClientRequest {

    private String name;
    private String cpf;
    private String isActive;
    private String cep;
    private String email;
    private String password;
    private String repeatPassword;
    private String number;
    private String complement;
    
}
