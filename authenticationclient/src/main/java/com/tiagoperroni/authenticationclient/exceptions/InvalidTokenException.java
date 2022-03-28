package com.tiagoperroni.authenticationclient.exceptions;

public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException(String msg) {
        super(msg);
    }
}
