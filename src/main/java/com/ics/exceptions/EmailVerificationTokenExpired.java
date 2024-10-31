package com.ics.exceptions;

public class EmailVerificationTokenExpired extends RuntimeException {
    public EmailVerificationTokenExpired(String message) {
        super(message);
    }
}
