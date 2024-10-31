package com.ics.interfaces.services;

import com.ics.entities.Account;

public interface IEmailVerificationTokenService {
    String generateEmailVerificationToken(Account account);
    String consumeEmailVerificationToken(String token);
}
