package com.tiagoperroni.order.models;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ClientRequest {

    private Integer id;
    private String name;
    private String cpf;
    private String email;
    private String password;
    private Boolean isActive;
    private String clientPort;
  
    private AdressRequest adress;
    
}
