package com.example.usermanagement.exceptions;

public class EmailVerificationTokenExpired extends RuntimeException {
    public EmailVerificationTokenExpired(String message) {
        super(message);
    }
}
