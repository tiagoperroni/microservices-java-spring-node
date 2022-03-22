package com.tiagoperroni.ordermail.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private String OrderDate;
    
}
