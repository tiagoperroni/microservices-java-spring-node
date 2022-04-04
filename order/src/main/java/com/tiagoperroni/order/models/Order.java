package com.tiagoperroni.order.models;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    // @OneToMany(targetEntity = OrderItems.class, fetch = FetchType.LAZY, cascade =
    // CascadeType.ALL)
    @Embedded
    private List<OrderItems> items;
    private int quantityTotal;
    private Double totalPrice;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime orderDate;

    public Order() {
    }

    public Order(Integer id, String clientName, String clientCpf, String clientEmail, String cep,
            String numberOfHouse, List<OrderItems> items, int quantityTotal, Double totalPrice, LocalDateTime orderDate) {
    }
}
