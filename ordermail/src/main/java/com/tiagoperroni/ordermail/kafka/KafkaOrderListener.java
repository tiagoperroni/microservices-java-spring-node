package com.tiagoperroni.ordermail.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagoperroni.ordermail.model.OrderMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderListener {
    
    private Logger logger = LoggerFactory.getLogger(KafkaOrderListener.class);

    @KafkaListener(topics = "${topic.shop-order}", groupId = "${spring.kafka.consumer.group-id}")
    public void getMessage(String message) throws JsonMappingException, JsonProcessingException {
        logger.info("New order was received: {}", message);

        ObjectMapper mapper = new ObjectMapper();
        var orderReceived = mapper.readValue(message, OrderMessage.class);

        System.out.println(orderReceived);
    }
}
