package com.tiagoperroni.client.model;

import lombok.Data;

@Data
public class AdressRequest {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    
}
