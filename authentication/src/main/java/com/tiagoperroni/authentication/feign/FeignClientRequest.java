package com.tiagoperroni.authentication.feign;

import com.tiagoperroni.authentication.models.ClientLogin;
import com.tiagoperroni.authentication.models.ClientRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "client")
public interface FeignClientRequest {

    @PostMapping("/clients/login")
    public ResponseEntity<ClientRequest> clientLogin(@RequestBody ClientLogin clientLogin);
    
}
