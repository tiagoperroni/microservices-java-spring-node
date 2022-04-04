package com.tiagoperroni.order.builder;

import com.tiagoperroni.order.models.Order;
import static org.junit.jupiter.api.Assertions.*;

import com.tiagoperroni.order.models.OrderItems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;

public class OrderBuilderTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOrderBuilder() {
        Order order = new OrderBuilder()
                .id(1).clientName("Tiago").clientCpf("12345")
                .clientEmail("tiago@gmail.com").cep("8712")
                .orderDate(LocalDateTime.now()).numberOfHouse("123")
                .items(Arrays.asList(new OrderItems())).quantityTotal(2).totalPrice(10.0).build();

        assertEquals("Tiago", order.getClientName());
    }
}
