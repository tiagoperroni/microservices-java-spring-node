// package com.tiagoperroni.ordermail.kafka;

// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Service;

// import lombok.extern.log4j.Log4j2;

// @Log4j2
// @Service
// public class KafkaOrderListener {
    
//     @KafkaListener(topics = "${topic.shop-order}", groupId = "${spring.kafka.consumer.group-id}")
//     public void getMessage(String message) {
//         log.info("=== New message received: {}", message);
//     }
// }
