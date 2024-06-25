package com.teknokote.cm.controller.handler;

import com.teknokote.core.exceptions.EntityNotFoundException;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.exceptions.UnexpectedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.ResponseEntity.badRequest;
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFound(EntityNotFoundException exception) {
        return badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<String> unexceptedException(UnexpectedException exception) {
        return badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(ServiceValidationException.class)
    public ResponseEntity<String> serviceValidationException(ServiceValidationException exception) {
        return badRequest().body(exception.getMessage());
    }
}
