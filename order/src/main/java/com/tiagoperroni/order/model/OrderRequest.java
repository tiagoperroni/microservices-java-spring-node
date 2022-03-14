package com.tiagoperroni.order.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {

    private List<ProductList> productList = new ArrayList<>();
    private Integer clientId;
    private Integer quantity;  
    
    
}
