package com.tiagoperroni.order.models;

import lombok.Data;

@Data
public class AdressRequest {

    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;

}
