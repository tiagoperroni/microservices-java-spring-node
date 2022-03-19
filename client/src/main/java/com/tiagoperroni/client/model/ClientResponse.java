package com.tiagoperroni.client.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "tb_client")
public class ClientResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Integer id;
    private String name;
    private String cpf;
    private Boolean isActive;
    private String clientPort; 
   
    @Embedded
    private AdressResponse adress;
    
}
