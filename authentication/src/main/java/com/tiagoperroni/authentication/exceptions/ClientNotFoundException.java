package com.tiagoperroni.authentication.exceptions;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String message) {
        super(message);
    }
}
