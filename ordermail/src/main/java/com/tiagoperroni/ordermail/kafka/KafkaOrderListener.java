package com.tiagoperroni.ordermail.kafka;

import javax.mail.MessagingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagoperroni.ordermail.email.EmailService;
import com.tiagoperroni.ordermail.model.OrderItems;
import com.tiagoperroni.ordermail.model.OrderMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderListener {

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(KafkaOrderListener.class);

    @KafkaListener(topics = "${topic.shop-order}", groupId = "${spring.kafka.consumer.group-id}")
    public void getMessage(String message) throws JsonMappingException, JsonProcessingException, MessagingException {
        logger.info("New order was received: {}", message);

        ObjectMapper mapper = new ObjectMapper();

        var orderReceived = mapper.readValue(message, OrderMessage.class);

        this.emailService.sendMail(orderReceived.getClientEmail(), "Seu pedido foi recebido!",
                this.textMessage(orderReceived), "arquivos/invoice.pdf");

    }

    public String textMessage(OrderMessage orderMessage) {
        String concat = "";
        for (OrderItems item : orderMessage.getOrderItems()) {
            concat += "  Produto: " + item.getProductName() + "\n"
                    + "  Quantidade: " + item.getQuantity() + "\n"
                    + "  Preço Unitário: " + item.getProductPrice() + "\n"
                    + "\n";

        }
        return "Olá " + orderMessage.getClientName() + ", obrigado por sua compra! "
                + "Segue os detalhes do seu pedido abaixo!" + "\n"
                + "\n"
                + "Id: " + orderMessage.getId() + "\n"
                + "  Nome: " + orderMessage.getClientName() + "\n"
                + "  Email: " + orderMessage.getClientEmail() + "\n"
                + "  CPF: " + orderMessage.getClientCpf() + "\n"
                + "\n"
                + "===============================" + "\n"
                + "Seus Produtos: " + "\n"
                + concat
                + "===============================" + "\n"
                + "\n"
                + "Quantidade Total: " + orderMessage.getTotalQuantity() + "\n"
                + "Valor Total: " + orderMessage.getTotalOrder() + "\n"
                + "Data do Pedido: " + orderMessage.getOrderDate() + "\n";
    }
}
