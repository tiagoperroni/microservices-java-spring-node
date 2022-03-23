package com.tiagoperroni.authentication.models;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientRequest {

    private String cpf;
    private String email;
    private String password;
        
}
