package com.tiagoperroni.order.controller;

import com.tiagoperroni.order.model.OrderRequest;
import com.tiagoperroni.order.model.OrderResponse;
import com.tiagoperroni.order.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class ProductController {

    @Autowired
    private OrderService orderService;    

    @PostMapping
    public ResponseEntity<OrderResponse> getProduct(@RequestBody OrderRequest request) {
        return new ResponseEntity<>(orderService.makeOrder(request), HttpStatus.CREATED);
    }
}
