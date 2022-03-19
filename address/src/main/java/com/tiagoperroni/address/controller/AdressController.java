package com.tiagoperroni.address.controller;

import java.util.HashMap;
import java.util.Map;

import com.tiagoperroni.address.model.Adress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/adress")
public class AdressController {
    
    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(AdressController.class);

    @GetMapping("/{cep}")
    @Retry(name = "requestAdress", fallbackMethod = "requestAdressFallback")
    public ResponseEntity<Adress> getAdress(@PathVariable("cep") String cep) {
        logger.info("Request received cep: {}", cep);
        ResponseEntity<Adress> adress = restTemplate
                .getForEntity("http://viacep.com.br/ws/" + cep + "/json/", Adress.class);
        logger.info("Sending adress: {}", adress);
        return new ResponseEntity<>(adress.getBody(), HttpStatus.OK);
        
    }

    public ResponseEntity<Map<String, String>> requestAdressFallback(Throwable ex) {
        var error = new HashMap<String, String>();
        error.put("Error", "Fail when try to send a request to CEP API");
        error.put("OriginalMessage", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

