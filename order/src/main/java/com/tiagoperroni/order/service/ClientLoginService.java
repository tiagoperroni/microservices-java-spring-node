package com.tiagoperroni.order.service;

import java.util.UUID;

import com.tiagoperroni.order.exceptions.ClientNotFoundException;
import com.tiagoperroni.order.exceptions.InvalidPasswordException;
import com.tiagoperroni.order.exceptions.InvalidTokenException;
import com.tiagoperroni.order.feign.ClientFeignRequest;
import com.tiagoperroni.order.model.ClientLogin;
import com.tiagoperroni.order.model.ClientLoginToken;
import com.tiagoperroni.order.model.ClientRequest;
import com.tiagoperroni.order.model.OrderRequest;
import com.tiagoperroni.order.repository.ClientLoginTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoginService {

    @Autowired
    private ClientLoginTokenRepository clientLoginTokenRepository;

    @Autowired
    private ClientFeignRequest feignRequest; 
    
    /**
     * inicio de login do client
     * @param clientLogin
     * @return
     */

    public String clientLoginService(ClientLogin clientLogin) {
       try {
           var response = this.feignRequest.clientLogin(clientLogin).getBody();
           this.verifyPassword(response, clientLogin);
        if (response != null) {
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
                throw new ClientNotFoundException(String.format("Not found client with the e-mail %s.", clientLogin.getEmail()));
           } else {
                throw new ClientNotFoundException(String.format(e.getMessage()));
           }
       }
              
       return null;               
    }

    /**
     * verifica se password é valido
     * @param clientRequest
     * @param clientLogin
     */
    public void verifyPassword(ClientRequest clientRequest, ClientLogin clientLogin) {
        System.out.println(clientRequest +  " " + clientLogin);
        if (!clientRequest.getPassword().equals(clientLogin.getPassword())) {
            throw new InvalidPasswordException("The password informed do not math with this e-mail. Try again!");
        }
    }

    /***
     * Validando se token é valido
     */
    public void verifyTokenIsValid(OrderRequest orderRequest, String token) {        
        var clientLoginToken = this.clientLoginTokenRepository.findByEmail(orderRequest.getClientEmail());
        
        if (!clientLoginToken.getToken().equals(token)) {
            throw new InvalidTokenException("Token is invalid. Try login to get a new Token.");
        }
    }
    
}
