package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.entities.Account;

public interface IPasswordResetTokenService {
    String generatePasswordResetToken(Account account);
    String consumePasswordResetToken(String token);
}
