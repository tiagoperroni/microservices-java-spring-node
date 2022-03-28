package com.tiagoperroni.authenticationclient.controller;

import java.util.HashMap;
import java.util.Map;

import com.tiagoperroni.authenticationclient.models.ClientLogin;
import com.tiagoperroni.authenticationclient.service.AuthenticationService;
import com.tiagoperroni.authenticationclient.service.ClientLoginService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/login")
public class LoginClientController {

    @Autowired
    private ClientLoginService clientLoginService;

    @Autowired
    private AuthenticationService authenticationService;

    private Logger logger = LoggerFactory.getLogger(LoginClientController.class);

    @PostMapping
    @Retry(name = "retryForClientRequest", fallbackMethod = "retryForClientRequestFallBack")
    public ResponseEntity<String> clientLogin(@RequestBody ClientLogin clientLogin) {
        logger.info("ClientLoginController request with e-mail: {}", clientLogin.getEmail());
        return new ResponseEntity<>(this.clientLoginService.clientLoginService(clientLogin), HttpStatus.ACCEPTED);
    }

    private ResponseEntity<Map<String, String>> retryForClientRequestFallBack(Throwable ex) {
        var error = new HashMap<String, String>();
        error.put("Message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);

    }

    @GetMapping("/token/{email}")
    public ResponseEntity<String> getToken(@PathVariable("email") String email) {
        logger.info("New client token request.");
        return new ResponseEntity<>(this.authenticationService.getToken(email), HttpStatus.OK);
    }

}
