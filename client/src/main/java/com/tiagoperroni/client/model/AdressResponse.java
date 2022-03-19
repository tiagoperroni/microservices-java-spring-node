package com.tiagoperroni.client.model;

import lombok.Data;

@Data
public class AdressResponse {

    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro; 
    private String cidade;      
    private String uf;
    
}
