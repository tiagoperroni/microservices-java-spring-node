package com.tiagoperroni.order.service;

import com.tiagoperroni.order.exceptions.InvalidTokenException;
import com.tiagoperroni.order.feign.AuthenticationFeignClient;
import com.tiagoperroni.order.models.ClientLogin;
import com.tiagoperroni.order.models.OrderRequest;
import com.tiagoperroni.order.repository.ClientLoginTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoginService {

    @Autowired
    private ClientLoginTokenRepository clientLoginTokenRepository;

    @Autowired
    private AuthenticationFeignClient authenticationFeignClient;

    /**
     * inicio de login do client
     * 
     * @param clientLogin
     * @return
     */

    public String clientLoginService(ClientLogin clientLogin) {
        var token = this.authenticationFeignClient.clientLogin(clientLogin).getBody();
        return token;
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
