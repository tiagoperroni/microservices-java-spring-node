package com.tiagoperroni.client.controller;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.model.ClientLogin;
import com.tiagoperroni.client.model.ClientRequest;
import com.tiagoperroni.client.model.ClientResponse;
import com.tiagoperroni.client.model.ClientResponseLogin;
import com.tiagoperroni.client.service.ClientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    private Logger logger = LoggerFactory.getLogger(ClientController.class);    

    @PostMapping("/login/{email}")
    public ResponseEntity<ClientResponseLogin> getClientLogin(@PathVariable String email) {
        var clientResponse = clientService.getClientLogin(email);
        logger.info("Recebendo requisição para login: {}", clientResponse);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    @Retry(name = "requestAdressClient", fallbackMethod = "requestLoginFallBack")
    public ResponseEntity<String> clientLogin(@RequestBody ClientLogin clientLogin) {      
        logger.info("CONTROLLER: Recebendo requisição para login método clientLogin: {}");
        return new ResponseEntity<>(this.clientService.clientLogin(clientLogin), HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> requestLoginFallBack(Throwable ex) {
        var error = new HashMap<String, String>();
        if (ex.getMessage().contains("The password informed do not math with this e-mail.")) {
            error.put("Error", "The password informed do not math with this e-mail.");
            error.put("StatusCode", "400");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); 
        }       
            error.put("Error", ex.getMessage());
            error.put("StatusCode", "400");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);      
    }

    @GetMapping("/{email}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable String email) {
        var clientResponse = clientService.getClient(email);
        logger.info("Recebendo requisição: {}", clientResponse);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @PostMapping
    @Retry(name = "requestAdressClient", fallbackMethod = "requestAdressClientFallback")
    public ResponseEntity<ClientResponse> requestClient(@RequestBody ClientRequest request) {
        return new ResponseEntity<>(clientService.saveClient(request), HttpStatus.CREATED);
    }

    @PutMapping("/{email}")
    public ResponseEntity<String> updateClient(@PathVariable("email") String email, @RequestBody ClientRequest request,
            @RequestParam String token) {
        return new ResponseEntity<>(clientService.updateClient(email, request, token), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Map<String, String>> deleteClient(@PathVariable("email") String email,
            @RequestParam String token) {
        return new ResponseEntity<>(this.clientService.deleteClient(email, token), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Map<String, String>> requestAdressClientFallback(Throwable ex) {
        var error = new HashMap<String, String>();
        if (ex.getMessage().contains("Already exists a client with the CPF informed.")) {
            error.put("Error", ex.getMessage());
            error.put("StatusCode", "400");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } else {
            error.put("Error", ex.getMessage());
            error.put("StatusCode", "503");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/properties")
    public ResponseEntity<Map<String, Object>> getClientProperties() throws JsonProcessingException {
        return new ResponseEntity<>(clientService.getPropertiesDetails(), HttpStatus.OK);
    }
}
