package com.tiagoperroni.client.exceptions;

import com.tiagoperroni.client.exceptions.ClientNotFoundException;
import com.tiagoperroni.client.exceptions.MessageError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<MessageError> clientNotFound(ClientNotFoundException exception) {
        var error = new MessageError();
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        error.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
