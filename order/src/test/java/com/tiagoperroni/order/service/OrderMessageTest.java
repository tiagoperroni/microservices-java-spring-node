package com.tiagoperroni.order.service;

import com.tiagoperroni.order.kafka.KafkaProducerConfig;
import com.tiagoperroni.order.models.ClientRequest;
import com.tiagoperroni.order.models.OrderItems;
import com.tiagoperroni.order.models.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

public class OrderMessageTest {

    @Mock
    private KafkaProducerConfig kafkaProducerConfig;

    @InjectMocks
    private OrderMessageService orderMessageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendMessage() {

        doNothing().when(this.kafkaProducerConfig).sendMessage(Mockito.anyString());

        var message = new OrderResponse();
        message.setClient(new ClientRequest());
        message.setId(1);
        message.setOrderDate("12");
        message.setItems(Arrays.asList(new OrderItems()));
        this.orderMessageService.sendOrderMessage(message);
    }
}
