package com.tiagoperroni.client.repository;

import java.util.Optional;

import com.tiagoperroni.client.model.ClientResponse;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientResponse, Integer> {

    Optional<ClientResponse> findByCpf(String cpf);    
}
