package com.tiagoperroni.order.feign;

import com.tiagoperroni.order.models.ClientRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client")
public interface ClientFeignRequest {
    
    @GetMapping("/clients/{email}")
    public ResponseEntity<ClientRequest> getClient(@PathVariable("email") String email);
    
}
