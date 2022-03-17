package com.tiagoperroni.order.feign;

import com.tiagoperroni.order.model.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client")
public interface ClientFeignRequest {
    
    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClient(@PathVariable("id") Integer id);
}
