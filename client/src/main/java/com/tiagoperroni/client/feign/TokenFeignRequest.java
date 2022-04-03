package com.tiagoperroni.client.feign;

import com.tiagoperroni.client.model.ClientLogin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authenticationclient")
public interface TokenFeignRequest {

    @GetMapping("/login/token/{email}")
    public ResponseEntity<String> getToken(@PathVariable("email") String email);

    @PostMapping("/login")
    public ResponseEntity<String> getTokenLogin(@RequestBody ClientLogin clientLogin);

}
