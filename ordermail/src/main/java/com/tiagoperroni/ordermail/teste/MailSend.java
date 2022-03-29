package com.tiagoperroni.ordermail.teste;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSend {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String title, String text) {
        var message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(title);
        message.setText(text);

        this.javaMailSender.send(message);
    }
}
