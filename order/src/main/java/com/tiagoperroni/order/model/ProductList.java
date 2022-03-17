package com.tiagoperroni.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductList {
    
    private Integer id;
    private Integer quantity;
}
