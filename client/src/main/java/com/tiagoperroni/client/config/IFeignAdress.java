package com.tiagoperroni.client.config;

import com.tiagoperroni.client.model.AdressRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "adress-api", url = "http://localhost:8090/adress")
public interface IFeignAdress {
    
    @GetMapping("/{cep}")
    public ResponseEntity<AdressRequest> getAdress(@PathVariable("cep") String cep);
}
