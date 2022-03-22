package com.tiagoperroni.ordermail.model;

import lombok.Data;

@Data
public class OrderItems {

    private Integer id;
    
    private Integer productId;
    private String productName;
    private Double productPrice;
    private int quantity; 

}
