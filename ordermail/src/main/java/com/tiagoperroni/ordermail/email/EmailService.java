package com.tiagoperroni.ordermail.email;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;    
    }

    public void sendMail(String to, String title, String text, String file) throws MessagingException {
        logger.info("Starting to prepare a new order mail");
      

        var mensagem = javaMailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mensagem, true);

        helper.setTo(to);
        helper.setSubject(title);
        helper.setText(text);      
        helper.addAttachment("invoice.pdf", new ClassPathResource(file));
        
        javaMailSender.send(mensagem);
        logger.info("Mail was ready to send to {}", to);
    }

}
