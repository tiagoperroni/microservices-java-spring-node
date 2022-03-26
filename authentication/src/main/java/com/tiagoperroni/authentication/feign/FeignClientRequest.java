package com.tiagoperroni.authentication.feign;

import com.tiagoperroni.authentication.models.ClientRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "client")
public interface FeignClientRequest {

    @PostMapping("/clients/login/{email}")
    public ResponseEntity<ClientRequest> clientLogin(@PathVariable("email") String email);

}
