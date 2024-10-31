package com.example.usermanagement.services;

import com.example.usermanagement.entities.Account;
import com.example.usermanagement.entities.EmailVerificationToken;
import com.example.usermanagement.exceptions.BadRequestException;
import com.example.usermanagement.exceptions.EmailVerificationTokenExpired;
import com.example.usermanagement.interfaces.services.IEmailVerificationTokenService;
import com.example.usermanagement.repositories.EmailVerificationTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService implements IEmailVerificationTokenService{

    @Value("${app.email.verification.token.expiryInSec}")
    private int emailVerificationTokenExpiryInSec;
    @Value("${app.email.verification.token.maxTokenGenerationInSec}")
    private int maxTokenGenerationInSec;

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    public String generateEmailVerificationToken(Account account) {
        var existingToken = emailVerificationTokenRepository.findByAccount(account);

        if (existingToken.isPresent()) {
            var token = existingToken.get();

            if(token.isUsed()) {
                throw new BadRequestException("Email verification token has already been used");
            }

            // check if the token have been generated
            if (token.createdInLastSeconds(maxTokenGenerationInSec)) {
                Instant canGenerateTokenAfterInstant = token.getCreatedDate().plusSeconds(maxTokenGenerationInSec);
                LocalDateTime canGenerateTokenAfter = LocalDateTime.ofInstant(canGenerateTokenAfterInstant, ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma dd/MM/yyyy");
                throw new BadRequestException("A New Token can be generated after " + canGenerateTokenAfter.format(formatter));
            }

            emailVerificationTokenRepository.delete(token);
        }

        var newToken = new EmailVerificationToken(account, emailVerificationTokenExpiryInSec);
        emailVerificationTokenRepository.save(newToken);
        return newToken.getToken();
    }

    public String consumeEmailVerificationToken(String token) {
        var emailVerificationToken = emailVerificationTokenRepository.findByToken(token).orElseThrow(
                () -> new EntityNotFoundException("Email verification token not found"));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
        if(emailVerificationToken.isExpired()) {
            throw new EmailVerificationTokenExpired("Email verification token has expired");
        }

        return emailVerificationToken.getAccount().getEmail();
    }

}
