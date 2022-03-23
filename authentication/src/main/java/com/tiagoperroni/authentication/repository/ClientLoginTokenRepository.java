package com.tiagoperroni.authentication.repository;

import com.tiagoperroni.authentication.models.ClientLoginToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientLoginTokenRepository
        extends JpaRepository<com.tiagoperroni.authentication.models.ClientLoginToken, Integer> {

    ClientLoginToken findByEmail(String email);
}
