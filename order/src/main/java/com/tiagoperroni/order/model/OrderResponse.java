package com.tiagoperroni.order.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OrderResponse {

    private String id;
    private Client client;
    private List<OrderItems> items;    
    private int quantityTotal;
    private Double totalPrice;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime orderDate;
    
}
