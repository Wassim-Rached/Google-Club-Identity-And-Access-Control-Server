package com.ics.exceptions;

public class EmailVerificationToken extends RuntimeException {
    public EmailVerificationToken(String message) {
        super(message);
    }
}
