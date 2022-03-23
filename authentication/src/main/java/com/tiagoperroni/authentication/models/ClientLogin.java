package com.tiagoperroni.authentication.models;

import lombok.Data;

@Data
public class ClientLogin {

    private String email;
    private String password;
    
}
