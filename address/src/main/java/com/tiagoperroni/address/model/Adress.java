package com.tiagoperroni.address.model;

import lombok.Data;

@Data
public class Adress {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

    
}
