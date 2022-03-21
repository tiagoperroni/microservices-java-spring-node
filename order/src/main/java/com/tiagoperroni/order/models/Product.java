package com.tiagoperroni.order.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class Product {
    
    private Integer id;
    private String name;
    private Double price;
    private Integer stock;
}
