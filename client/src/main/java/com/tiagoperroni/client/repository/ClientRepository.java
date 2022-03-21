package com.tiagoperroni.client.repository;

import java.util.Optional;

import com.tiagoperroni.client.model.Client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByCpf(String cpf);    
    Optional<Client> findByEmail(String email);    
}
