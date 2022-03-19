package com.tiagoperroni.client.model;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import lombok.Data;

@Data
@Embeddable
public class AdressResponse {     
 
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro; 
    private String cidade;      
    private String uf;
    
}
