package com.tiagoperroni.order.model;

import lombok.Data;

@Data
public class Client {

    private Integer id;
    private String name;
    private String cpf;
    private Boolean isActive;
    
}
