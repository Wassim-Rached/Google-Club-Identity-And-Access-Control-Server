package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.entities.Account;

public interface IEmailVerificationTokenService {
    String generateEmailVerificationToken(Account account);
    String consumeEmailVerificationToken(String token);
}
