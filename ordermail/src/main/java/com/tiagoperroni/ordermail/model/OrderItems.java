package com.tiagoperroni.ordermail.model;

import lombok.Data;

@Data
public class OrderItems {
        
    private String productName;
    private Double productPrice;
    private int quantity;
    private Double total; 

}
