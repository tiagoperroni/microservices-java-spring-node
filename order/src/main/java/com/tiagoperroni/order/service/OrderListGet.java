package com.tiagoperroni.order.service;

import java.util.List;

import com.tiagoperroni.order.models.Order;
import com.tiagoperroni.order.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderListGet {    

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrderByEmail(String email) {        
        return this.orderRepository.findByClientEmail(email).orElse(null);
    }
}
