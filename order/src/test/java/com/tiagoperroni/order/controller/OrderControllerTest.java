package com.tiagoperroni.order.controller;

import com.tiagoperroni.order.builder.OrderBuilder;
import com.tiagoperroni.order.builder.OrderResponseBuilder;
import com.tiagoperroni.order.models.Order;
import com.tiagoperroni.order.models.OrderRequest;
import com.tiagoperroni.order.models.OrderResponse;
import com.tiagoperroni.order.service.OrderListGetService;
import com.tiagoperroni.order.service.OrderService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderListGetService orderListGetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOrdersByEmail() {

        Order order = new Order();
        order.setId(1); order.setClientName("Tiago"); order.setClientEmail("tiago@gmail.com");

        System.out.println(order.toString());

        when(this.orderListGetService.getOrderByEmail(anyString())).thenReturn(Arrays.asList(order));

        ResponseEntity<List<Order>> getOrders = this.orderController.getOrderByEmail("tiago@gmail.com");
        assertEquals("Tiago", getOrders.getBody().get(0).getClientName());
    }

    @Test
    public void testRetryForClientRequestFallBack() {

        var error = new Throwable();
        ResponseEntity<Map<String, String>> message = this.orderController.retryForClientRequestFallBack(error);
        assertTrue(message.getBody().containsKey("message"));
        assertTrue(message.getBody().containsValue("authentication microservice is down."));
        assertEquals("503 SERVICE_UNAVAILABLE", message.getStatusCode().toString());
    }

    @Test
    public void testMakeOrder() {

        var orderResponse = new OrderResponseBuilder()
                .id(1).totalPrice(25.00).build();

        when(this.orderService.makeOrder(any(), anyString())).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = this.orderController.makeOrder(new OrderRequest(), "1234");
        assertEquals(1, response.getBody().getId());
        assertEquals(25.0, response.getBody().getTotalPrice());

    }

    @Test
    public void testOrdersFallBack_ProductError() {
        ResponseEntity<Map<String, String>> response = this.orderController.ordersFallBack(new Throwable("product"));
        assertTrue(response.getBody().containsKey("original"));
        assertTrue(response.getBody().containsValue("Microservice Product-Api is down."));

    }

    @Test
    public void testOrdersFallBack_ClientError() {
        ResponseEntity<Map<String, String>> response = this.orderController.ordersFallBack(new Throwable("client"));
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().containsValue("Microservice Client-Api is down."));

    }

    @Test
    public void testOrdersFallBack_BadId() {
        ResponseEntity<Map<String, String>> response = this.orderController.ordersFallBack(new Throwable("with id"));
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().containsValue("with id"));

    }

    @Test
    public void testOrdersFallBack() {
        ResponseEntity<Map<String, String>> response = this.orderController.ordersFallBack(new Throwable("aaa"));
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().containsValue("aaa"));

    }

}
