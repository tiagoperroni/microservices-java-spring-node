package com.tiagoperroni.order.exceptions;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String message) {
        super(message);
    }
}
