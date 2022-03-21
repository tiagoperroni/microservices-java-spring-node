package com.tiagoperroni.order.models;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   
   
    private String clientName;
    private String clientCpf;
    private String clientEmail;
    private String cep;
    private String numberOfHouse;


    @OneToMany(targetEntity = OrderItems.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItems> items;
    private int quantityTotal;
    private Double totalPrice;
    private LocalDateTime orderDate;

}
