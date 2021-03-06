package com.tiagoperroni.authenticationclient.service;

import java.util.UUID;

import com.tiagoperroni.authenticationclient.exceptions.ClientNotFoundException;
import com.tiagoperroni.authenticationclient.exceptions.InvalidPasswordException;
import com.tiagoperroni.authenticationclient.exceptions.InvalidTokenException;
import com.tiagoperroni.authenticationclient.feign.FeignClientRequest;
import com.tiagoperroni.authenticationclient.models.ClientLogin;
import com.tiagoperroni.authenticationclient.models.ClientLoginToken;
import com.tiagoperroni.authenticationclient.models.ClientRequest;
import com.tiagoperroni.authenticationclient.repository.ClientLoginTokenRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientLoginService {

    @Autowired
    private FeignClientRequest clientRequest;

    Logger logger = LoggerFactory.getLogger(ClientLoginService.class);

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
        logger.info("ClientLoginService request with e-mail: {}", clientLogin.getEmail());
        try {
            var clientRequest = this.clientRequest.clientLogin(clientLogin.getEmail()).getBody();              
            if (clientRequest != null) {
                this.verifyPassword(clientRequest, clientLogin);
                String token = UUID.randomUUID().toString();
                token += UUID.randomUUID().toString();
                var clientLoginToken = new ClientLoginToken();
                clientLoginToken.setEmail(clientLogin.getEmail());
                clientLoginToken.setToken(token);
                this.clientLoginTokenRepository.deleteAll();
                var clientTokenResponse = this.clientLoginTokenRepository.save(clientLoginToken);
                logger.info("ClientLoginService response with token: {}", clientTokenResponse.getToken());
                return "Token: " + token;
            }
            if (clientRequest == null) throw new ClientNotFoundException("N??o foi encontrado registro com este e-mail.");
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
     * verifica se password ?? valido
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
     * Validando se token ?? valido
     */
    public void verifyTokenIsValid(String clientMail, String token) {
        var clientLoginToken = this.clientLoginTokenRepository.findByEmail(clientMail);

        if (!clientLoginToken.getToken().equals(token)) {
            throw new InvalidTokenException("Token is invalid. Try login to get a new Token.");
        }
    }

}
