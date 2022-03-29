package com.tiagoperroni.order.service;

import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagoperroni.order.kafka.KafkaProducerConfig;
import com.tiagoperroni.order.models.OrderMessage;
import com.tiagoperroni.order.models.OrderResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class OrderMessageService {

    @Autowired
    private KafkaProducerConfig kafka;

    public void sendOrderMessage(OrderResponse orderResponse) {

        log.info("== Start prepare order message ==");
        var orderMessage = new OrderMessage();
        orderMessage.setId(orderResponse.getId().toString());
        orderMessage.setClientName(orderResponse.getClient().getName());
        orderMessage.setClientCpf(orderResponse.getClient().getCpf());
        orderMessage.setClientEmail(orderResponse.getClient().getEmail());
        orderMessage.setOrderItems(orderResponse.getItems());
        orderMessage.setTotalQuantity(orderResponse.getQuantityTotal());
        orderMessage.setTotalOrder(orderResponse.getTotalPrice());
        orderMessage.setOrderDate(LocalDate.now().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String message;
        try {
            message = objectMapper.writeValueAsString(orderMessage);
            log.info("== Sending new Order to Kafka Broken ==: {}", orderMessage);
            this.kafka.sendMessage(message);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }
}
