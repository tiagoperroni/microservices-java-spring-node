package com.tiagoperroni.authentication.service;

import com.tiagoperroni.authentication.repository.ClientLoginTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    @Autowired
    private ClientLoginTokenRepository clientLoginTokenRepository;

    public String getToken(String email) {
       var response = this.clientLoginTokenRepository.findByEmail(email);
       return response.getToken();
    }
}
