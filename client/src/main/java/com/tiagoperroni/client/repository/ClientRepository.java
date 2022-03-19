package com.tiagoperroni.client.repository;

import com.tiagoperroni.client.model.ClientResponse;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientResponse, Integer>{
    
}
