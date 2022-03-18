package com.tiagoperroni.order.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerMain {
    
    @ExceptionHandler(StockNotAvaibleException.class)
    public ResponseEntity<MessageError> stockNotAvaible(StockNotAvaibleException exception) {
        var error = new MessageError(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientNotActiveException.class)
    public ResponseEntity<MessageError> clientNotActive(ClientNotActiveException exception) {
        var error = new MessageError(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<MessageError> clientNotFound(ClientNotFoundException exception) {
        var error = new MessageError();
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        error.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
