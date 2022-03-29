package com.tiagoperroni.ordermail.controller;
import com.tiagoperroni.ordermail.teste.MailSend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @Autowired
    private MailSend mailSend;

    @GetMapping
    public String teste() {
        this.mailSend.sendEmail("tiagoperroni86@gmail.com", "Email teste", "Texto teste aqui para enviar e-mail.");
        System.out.println("Enviando e-mail");
        return "OK";
    }
}
