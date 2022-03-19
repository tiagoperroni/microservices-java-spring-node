package com.tiagoperroni.client.controller;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.model.ClientResponse;
import com.tiagoperroni.client.service.ClientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    private Logger logger = LoggerFactory.getLogger(ClientController.class);

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Integer id) {
        this.clientService.getAdress("87710130", "290", "casa");
        var clientResponse = clientService.getClient(id);        
        logger.info("Recebendo requisição: {}", clientResponse);       
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @GetMapping("/properties")
    public ResponseEntity<Map<String, Object>> getClientProperties() throws JsonProcessingException {
        return new ResponseEntity<>(clientService.getPropertiesDetails(), HttpStatus.OK);
    }
}
