package com.tiagoperroni.order.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class OrderResponse {

    private Integer id;
    @JsonIgnoreProperties(value = { "password", "isActive" })
    private ClientRequest client;
    private List<OrderItems> items;
    private int quantityTotal;
    private Double totalPrice;  
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime orderDate;

    public OrderResponse() {
    }

    public OrderResponse(Integer id, ClientRequest client, List<OrderItems> items, int quantityTotal, Double totalPrice,
            LocalDateTime orderDate) {
        this.id = id;
        this.client = client;
        this.items = items;
        this.quantityTotal = quantityTotal;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }
}
