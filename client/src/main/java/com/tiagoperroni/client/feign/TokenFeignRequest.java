package com.tiagoperroni.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authentication")
public interface TokenFeignRequest {

    @GetMapping("/login/token/{email}")
    public ResponseEntity<String> getToken(@PathVariable("email") String email);
    
}
