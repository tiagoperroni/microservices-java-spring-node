package com.tiagoperroni.authentication.service;

import java.util.UUID;

import com.tiagoperroni.authentication.exceptions.ClientNotFoundException;
import com.tiagoperroni.authentication.exceptions.InvalidPasswordException;
import com.tiagoperroni.authentication.exceptions.InvalidTokenException;
import com.tiagoperroni.authentication.feign.FeignClientRequest;
import com.tiagoperroni.authentication.models.ClientLogin;
import com.tiagoperroni.authentication.models.ClientLoginToken;
import com.tiagoperroni.authentication.models.ClientRequest;
import com.tiagoperroni.authentication.repository.ClientLoginTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientLoginService {

    @Autowired
    private FeignClientRequest clientRequest;

    @Autowired
    private ClientLoginTokenRepository clientLoginTokenRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * inicio de login do client
     * 
     * @param clientLogin
     * @return
     */

    public String clientLoginService(ClientLogin clientLogin) {
        try {
            var clientRequest = this.clientRequest.clientLogin(clientLogin).getBody();
            this.verifyPassword(clientRequest, clientLogin);
            if (clientRequest != null) {
                String token = UUID.randomUUID().toString();
                token += UUID.randomUUID().toString();
                var clientLoginToken = new ClientLoginToken();
                clientLoginToken.setEmail(clientLogin.getEmail());
                clientLoginToken.setToken(token);
                this.clientLoginTokenRepository.deleteAll();
                this.clientLoginTokenRepository.save(clientLoginToken);
                return token;
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Not found client with")) {
                throw new ClientNotFoundException(
                        String.format("Not found client with the e-mail %s.", clientLogin.getEmail()));
            } else {
                throw new ClientNotFoundException(String.format(e.getMessage()));
            }
        }

        return null;
    }

    /**
     * verifica se password é valido
     * 
     * @param clientRequest
     * @param clientLogin
     */
    public void verifyPassword(ClientRequest clientRequest, ClientLogin clientLogin) {
        if (!passwordEncoder.matches(clientLogin.getPassword(), clientRequest.getPassword())) {
            throw new InvalidPasswordException("The password informed do not math with this e-mail. Try again!");
        }
    }

    /***
     * Validando se token é valido
     */
    public void verifyTokenIsValid(String clientMail, String token) {
        var clientLoginToken = this.clientLoginTokenRepository.findByEmail(clientMail);

        if (!clientLoginToken.getToken().equals(token)) {
            throw new InvalidTokenException("Token is invalid. Try login to get a new Token.");
        }
    }

}