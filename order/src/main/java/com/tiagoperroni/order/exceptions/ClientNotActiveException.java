package com.tiagoperroni.order.exceptions;

public class ClientNotActiveException extends RuntimeException {
    
    public ClientNotActiveException(String msg) {
        super(msg);
    }
}
