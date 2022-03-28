package com.tiagoperroni.order.feign;

import com.tiagoperroni.order.models.Product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-api")
public interface ProductFeignRequest {
    
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") String id);

    @GetMapping("/product/{id}/{stock}")
    public ResponseEntity<Product> updateStockRequest(@PathVariable("id") String id, @PathVariable("stock") int quantity);
}
