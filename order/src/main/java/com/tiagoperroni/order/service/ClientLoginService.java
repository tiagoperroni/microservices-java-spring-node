package com.tiagoperroni.order.service;

import com.tiagoperroni.order.exceptions.InvalidTokenException;
import com.tiagoperroni.order.feign.AuthenticationRequestToken;
import com.tiagoperroni.order.models.OrderRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoginService {   
    
    @Autowired
    private AuthenticationRequestToken authenticationRequestToken;

    /***
     * Validando se token é valido
     */
    public Boolean verifyTokenIsValid(OrderRequest orderRequest, String token) {
        var clientLoginToken = this.authenticationRequestToken.getToken(orderRequest.getClientEmail());

        if (!clientLoginToken.getBody().equals(token)) {
            throw new InvalidTokenException("Token is invalid. Try login to get a new Token.");
        } else {
            return true;
        }
    }

}
