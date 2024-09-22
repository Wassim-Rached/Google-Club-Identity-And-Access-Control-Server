package com.example.usermanagement.advices;


import com.example.usermanagement.dto.StandardApiResponse;
import com.example.usermanagement.enums.StandardApiStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, "Invalid value for parameter "+ e.getPropertyName() +". Expected type: "+ e.getRequiredType()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String rootCauseMessage = Objects.requireNonNull(e.getRootCause()).getMessage();

        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, rootCauseMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, e.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

}