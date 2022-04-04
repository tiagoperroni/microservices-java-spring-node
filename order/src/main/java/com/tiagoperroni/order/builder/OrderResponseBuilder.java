package com.tiagoperroni.order.builder;

import com.tiagoperroni.order.models.ClientRequest;
import com.tiagoperroni.order.models.OrderItems;
import com.tiagoperroni.order.models.OrderResponse;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseBuilder {

    private Integer id;
    private ClientRequest client;
    private List<OrderItems> items;
    private int quantityTotal;
    private Double totalPrice;
    private LocalDateTime orderDate;

    public OrderResponseBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public OrderResponseBuilder client(ClientRequest client) {
        this.client = client;
        return this;
    }

    public OrderResponseBuilder items(List<OrderItems> items) {
        this.items = items;
        return this;
    }

    public OrderResponseBuilder quantityTotal(Integer quantityTotal) {
        this.quantityTotal = quantityTotal;
        return this;
    }

    public OrderResponseBuilder totalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public OrderResponseBuilder orderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public OrderResponse build() {
        return new OrderResponse(id, client, items, quantityTotal, totalPrice, orderDate);
    }
}
