package com.tiagoperroni.order.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerConfig {
    
    Logger logger = LoggerFactory.getLogger(KafkaProducerConfig.class);

    @Value("topic.shop-order")
    private String topicShopOrder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {

        this.kafkaTemplate.send(topicShopOrder, message);
        logger.info("Sending message to topic_shop_order: {}", message);
    }
}
