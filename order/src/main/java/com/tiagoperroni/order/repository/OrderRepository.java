package com.tiagoperroni.order.repository;

import java.util.List;
import java.util.Optional;

import com.tiagoperroni.order.models.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<List<Order>> findByClientEmail(String email);
    
}
