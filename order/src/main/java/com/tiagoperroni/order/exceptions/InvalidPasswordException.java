package com.tiagoperroni.order.exceptions;

public class InvalidPasswordException extends RuntimeException {
    
    public InvalidPasswordException(String msg) {
        super(msg);
    }
}
