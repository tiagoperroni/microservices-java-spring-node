package com.tiagoperroni.order.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class OrderResponse {

    private Integer id;
    @JsonIgnoreProperties(value = {"password", "isActive"})
    private ClientRequest client;
    private List<OrderItems> items;    
    private int quantityTotal;
    private Double totalPrice;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime orderDate;
    
}
