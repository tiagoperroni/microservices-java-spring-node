package com.tiagoperroni.order.mapper;

import com.tiagoperroni.order.models.Order;
import com.tiagoperroni.order.models.OrderResponse;

public class OrderMapper {

    public static Order convertFromResponse(OrderResponse response) {
        var order = new Order();
        order.setId(response.getId());
        order.setClientName(response.getClient().getName());
        order.setClientEmail(response.getClient().getEmail());
        order.setClientCpf(response.getClient().getCpf());
        order.setCep(response.getClient().getAdress().getCep());
        order.setNumberOfHouse(response.getClient().getAdress().getNumero());
        order.setItems(response.getItems());
        order.setQuantityTotal(response.getQuantityTotal());
        order.setTotalPrice(response.getTotalPrice());
        order.setOrderDate(response.getOrderDate());
        return order;
    }
    
}
