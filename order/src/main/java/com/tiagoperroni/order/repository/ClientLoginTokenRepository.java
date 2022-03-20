package com.tiagoperroni.order.repository;

import com.tiagoperroni.order.model.ClientLoginToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientLoginTokenRepository extends JpaRepository<ClientLoginToken, Integer> {
    
    ClientLoginToken findByEmail(String email);
}
