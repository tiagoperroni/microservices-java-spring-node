package com.tiagoperroni.order.controller;

import java.util.HashMap;
import java.util.Map;

import com.tiagoperroni.order.model.OrderRequest;
import com.tiagoperroni.order.model.OrderResponse;
import com.tiagoperroni.order.service.OrderService;

import io.github.resilience4j.retry.annotation.Retry;
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
    @Retry(name = "retryForMakeOrder", fallbackMethod = "ordersFallBack")
    public ResponseEntity<OrderResponse> makeOrder(@RequestBody OrderRequest request) {
        return new ResponseEntity<>(orderService.makeOrder(request), HttpStatus.CREATED);
    }

    private ResponseEntity<Map<String, String>> ordersFallBack(Throwable ex) {
        var error = new HashMap<String, String>();
        if (ex.getMessage().contains("product")) {
            error.put("error", "Microservice Product-Api is down.");
            return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
        } else if (ex.getMessage().contains("client")) {
            error.put("error", "Microservice Client-Api is down.");
            return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            error.put("error", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
