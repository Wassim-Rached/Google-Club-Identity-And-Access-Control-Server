package com.ics.exceptions;

/*
    this exception is thrown when the
    client sends invalid input to the server
 */
public class InputValidationException extends RuntimeException {
    public InputValidationException(String message) {
        super(message);
    }
}