package com.example.usermanagement.interfaces.services;

public interface IEmailService {
    void sendEmail(String to, String subject, String body);
}
