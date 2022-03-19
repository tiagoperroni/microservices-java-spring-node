package com.tiagoperroni.address.controller;

import com.tiagoperroni.address.model.Adress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/adress")
public class AdressController {
    
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<Adress> getAdress(String cep) {
        ResponseEntity<Adress> adress = restTemplate
                .getForEntity("http://viacep.com.br/ws/" + cep + "/json/", Adress.class);
        return new ResponseEntity<>(adress.getBody(), HttpStatus.OK);
        
    }
}

