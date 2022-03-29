package com.tiagoperroni.ordermail.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMail(String to, String title, String text) throws MessagingException {
        logger.info("Starting to prepare a new order mail");

        var message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(text);
        this.javaMailSender.send(message);

        logger.info("EmailService: E-mail is ready! Sending e-mail to {}", to);

    }

    public void sendAttachMail(String to, String title, String text, String file) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(title);
        helper.setText(text);
        helper.addAttachment("invoice.pdf", new ClassPathResource(file));
        logger.info("EmailService: E-mail is ready! Sending e-mail to {}", to);
        javaMailSender.send(message);   

    }

}
