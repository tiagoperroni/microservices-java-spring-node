package com.tiagoperroni.order.feign;

import com.tiagoperroni.order.model.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-api", url = "http://localhost:8081/clients/")
public interface ClientFeignRequest {
    
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable("id") Integer id);
}
