package com.tiagoperroni.order.model;

import lombok.Data;

@Data
public class Product {
    
    private Integer id;
    private String name;
    private Double price;
    private Integer stock;
}
