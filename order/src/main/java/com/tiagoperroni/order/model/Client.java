package com.tiagoperroni.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private Integer id;
    private String name;
    private String cpf;
    private Boolean isActive;
    
}