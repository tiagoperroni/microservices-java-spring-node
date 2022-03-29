package com.tiagoperroni.order.models;

import javax.persistence.Embeddable;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderItems {
    
    private String productName;
    private Double productPrice;
    private int quantity;  
    private Double total; 

}
