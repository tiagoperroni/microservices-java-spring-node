package com.tiagoperroni.authentication.exceptions;

public class InvalidPasswordException extends RuntimeException {
    
    public InvalidPasswordException(String msg) {
        super(msg);
    }
}
