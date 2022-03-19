package com.tiagoperroni.order.feign;

import com.tiagoperroni.order.model.ClientRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client")
public interface ClientFeignRequest {
    
    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientRequest> getClient(@PathVariable("id") Integer id);
}
