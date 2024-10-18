package com.example.usermanagement.exceptions;

public class EmailVerificationToken extends RuntimeException {
    public EmailVerificationToken(String message) {
        super(message);
    }
}
