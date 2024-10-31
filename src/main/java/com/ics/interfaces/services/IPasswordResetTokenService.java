package com.ics.interfaces.services;

import com.ics.entities.Account;

public interface IPasswordResetTokenService {
    String generatePasswordResetToken(Account account);
    String consumePasswordResetToken(String token);
    String validatePasswordResetToken(String token);
}
