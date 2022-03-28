package com.tiagoperroni.authenticationclient.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerMain {

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<MessageError> invalidPassword(InvalidPasswordException exception) {
        var error = new MessageError();
        error.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(exception.getMessage());
        error.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<MessageError> invalidToken(InvalidTokenException exception) {
        var error = new MessageError();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exception.getMessage());
        error.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
