package com.tiagoperroni.ordermail.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    
    }

    public void sendMail(String to, String subject, String text) {
        logger.info("Starting to prepare a new order mail");

        var newMessage = new SimpleMailMessage();

        newMessage.setTo(to);
        newMessage.setSubject(subject);
        newMessage.setText(text);
        this.javaMailSender.send(newMessage);
        logger.info("Mail was ready to send to {}", to);
    }

}
