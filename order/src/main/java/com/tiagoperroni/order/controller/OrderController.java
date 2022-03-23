package com.tiagoperroni.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tiagoperroni.order.models.ClientLogin;
import com.tiagoperroni.order.models.Order;
import com.tiagoperroni.order.models.OrderRequest;
import com.tiagoperroni.order.models.OrderResponse;
import com.tiagoperroni.order.service.ClientLoginService;
import com.tiagoperroni.order.service.OrderListGet;
import com.tiagoperroni.order.service.OrderService;

import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Products")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientLoginService clientLoginService;

    @Autowired
    private OrderListGet orderListGet;

    // private Logger logger = LoggerFactory.getLogger(OrderService.class);

    @ApiOperation(value = "Obtendo pedidos através do e-mail do cliente")
    @GetMapping("/{email}")
    public ResponseEntity<List<Order>> getOrderByEmail(@PathVariable String email) {
        // logger.info("New client login was received with cpf: {}", cpf);
        return new ResponseEntity<>(this.orderListGet.getOrderByEmail(email), HttpStatus.OK);
    }

    @ApiOperation(value = "Faça o login aqui para gerar o Token - Necessário Cadastro")
    @PostMapping("/login")
    @Retry(name = "retryForClientLogin", fallbackMethod = "retryForClientRequestFallBack")
    public ResponseEntity<String> clientLogin(@RequestBody ClientLogin clientLogin) {
        // logger.info("New client login was received with cpf: {}", cpf);
        return new ResponseEntity<>(this.clientLoginService.clientLoginService(clientLogin), HttpStatus.ACCEPTED);
    }

    private ResponseEntity<Map<String, String>> retryForClientRequestFallBack(Throwable ex) {
        var error = new HashMap<String, String>();
        error.put("message", "authentication microservice is down.");
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);

    }

    @ApiOperation(value = "Faça seu pedido aqui - Necessário login e para gerar Token")
    @PostMapping
    @Retry(name = "retryForMakeOrder", fallbackMethod = "ordersFallBack")
    public ResponseEntity<OrderResponse> makeOrder(@RequestBody OrderRequest request, @RequestParam String token) {
        return new ResponseEntity<>(orderService.makeOrder(request, token), HttpStatus.CREATED);
    }

    private ResponseEntity<Map<String, String>> ordersFallBack(Throwable ex) {
        var error = new HashMap<String, String>();
        if (ex.getMessage().contains("product")) {
            error.put("error", "Microservice Product-Api is down.");
            return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
        } else if (ex.getMessage().contains("client")) {
            error.put("error", "Microservice Client-Api is down.");
            return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
        } else if (ex.getMessage().contains("with id")) {
            error.put("error", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            error.put("error", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
