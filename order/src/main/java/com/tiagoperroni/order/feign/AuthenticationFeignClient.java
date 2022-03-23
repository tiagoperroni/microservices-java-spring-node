package com.tiagoperroni.order.feign;

import com.tiagoperroni.order.models.ClientLogin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authentication")
public interface AuthenticationFeignClient {

    @PostMapping("/login")
    public ResponseEntity<String> clientLogin(@RequestBody ClientLogin clientLogin);

}
