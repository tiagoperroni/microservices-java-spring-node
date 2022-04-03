package com.tiagoperroni.order.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
// import com.fasterxml.jackson.databind.annotation.JsonSerialize;
// import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
// import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;

@Data
public class OrderResponse {

    private Integer id;
    @JsonIgnoreProperties(value = { "password", "isActive" })
    private ClientRequest client;
    private List<OrderItems> items;
    private int quantityTotal;
    private Double totalPrice;
    // @JsonSerialize(using = LocalDateSerializer.class)
    // @JsonDeserialize(using = LocalDateDeserializer.class)
    // @JsonFormat(pattern = "dd/MM/yyyy")
    private String orderDate;

    public OrderResponse() {
    }

    public OrderResponse(Integer id, ClientRequest client, List<OrderItems> items, int quantityTotal, Double totalPrice, String orderDate) {
        this.id = id;
        this.client = client;
        this.items = items;
        this.quantityTotal = quantityTotal;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }
}
