package com.tiagoperroni.authenticationclient.repository;

import com.tiagoperroni.authenticationclient.models.ClientLoginToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientLoginTokenRepository
        extends JpaRepository<com.tiagoperroni.authenticationclient.models.ClientLoginToken, Integer> {

    ClientLoginToken findByEmail(String email);
}
