package com.tiagoperroni.order.models;

import java.util.List;

import lombok.Data;

@Data
public class OrderMessage {

    private String id;
    private String clientName;
    private String clientCpf;
    private String clientEmail;

    private List<OrderItems> orderItems;

    private Integer totalQuantity;
    private Double totalOrder;
    private String OrderDate;
    
}
