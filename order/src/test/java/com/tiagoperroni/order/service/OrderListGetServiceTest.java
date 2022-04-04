package com.tiagoperroni.order.service;

import com.tiagoperroni.order.models.Order;
import com.tiagoperroni.order.repository.OrderRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderListGetServiceTest {

    @InjectMocks
    private OrderListGetService orderListGetService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOrderByEmail() {

        var order = new Order();
        order.setClientName("Tiago");
        Optional<List<Order>> listOrders = Optional.of(new ArrayList<>());
        listOrders.get().add(order);
        when(this.orderRepository.findByClientEmail(anyString())).thenReturn(listOrders);

        List<Order> orders = this.orderListGetService.getOrderByEmail("tiago@gmail.com");

        assertEquals("Tiago", orders.get(0).getClientName());
    }
}
