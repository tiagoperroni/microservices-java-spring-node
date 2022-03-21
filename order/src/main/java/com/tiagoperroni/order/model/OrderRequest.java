package com.tiagoperroni.order.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private List<ProductList> productList = new ArrayList<>();   
    private String clientEmail;
    
}
