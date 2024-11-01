package com.ics.services;

import com.ics.entities.Account;
import com.ics.entities.PasswordResetToken;
import com.ics.exceptions.BadRequestException;
import com.ics.interfaces.services.IPasswordResetTokenService;
import com.ics.repositories.PasswordResetTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService implements IPasswordResetTokenService {

    @Value("${app.password.reset.token.expiryInSec}")
    private int passwordResetTokenExpiryInSec;
    @Value("${app.password.reset.token.maxTokenGenerationInSec}")
    private int maxTokenGenerationInSec;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    @Transactional
    public String generatePasswordResetToken(Account account) {
        var existingToken = passwordResetTokenRepository.findByAccount(account);

        if (existingToken.isPresent()) {
            var token = existingToken.get();

            // check if the token have been generated
            if (token.haveBeenCreatedLately(maxTokenGenerationInSec)) {
                Instant canGenerateTokenAfterInstant = token.getCreatedDate().plusSeconds(maxTokenGenerationInSec);
                LocalDateTime canGenerateTokenAfter = LocalDateTime.ofInstant(canGenerateTokenAfterInstant, ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma dd/MM/yyyy");
                throw new BadRequestException("A New Token can be generated after " + canGenerateTokenAfter.format(formatter));
            }
            account.setPasswordResetToken(null);
            token.setAccount(null);
            passwordResetTokenRepository.delete(token);
            passwordResetTokenRepository.flush();
        }

        return createNewPasswordResetToken(account);
    }

    private String createNewPasswordResetToken(Account account) {
        var newToken = new PasswordResetToken(account, passwordResetTokenExpiryInSec);
        passwordResetTokenRepository.save(newToken);
        return newToken.getToken();
    }

    @Override
    public String consumePasswordResetToken(String token) {
        var passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(
                () -> new EntityNotFoundException("Password reset token not found"));

        if(passwordResetToken.isExpired()) {
            throw new BadRequestException("Password reset token has expired");
        }

        if(passwordResetToken.getIsUsed()) {
            throw new BadRequestException("Password reset token has already been used");
        }

        passwordResetToken.setIsUsed(true);

        return passwordResetToken.getAccount().getEmail();
    }

    @Override
    public String validatePasswordResetToken(String token) {
        var passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(
                () -> new EntityNotFoundException("Password reset token not found"));

        if(passwordResetToken.isExpired()) {
            throw new BadRequestException("Password reset token has expired");
        }

        if(passwordResetToken.getIsUsed()) {
            throw new BadRequestException("Password reset token has already been used");
        }

        return passwordResetToken.getAccount().getEmail();
    }
}
