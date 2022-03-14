package com.tiagoperroni.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {

    private Integer productId;
    private String productName;
    private Double productPrice;
    private int quantity;
    
}
