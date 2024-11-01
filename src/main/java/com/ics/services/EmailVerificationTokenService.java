package com.ics.services;

import com.ics.entities.Account;
import com.ics.entities.EmailVerificationToken;
import com.ics.exceptions.BadRequestException;
import com.ics.exceptions.EmailVerificationTokenExpired;
import com.ics.interfaces.services.IEmailVerificationTokenService;
import com.ics.repositories.EmailVerificationTokenRepository;
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
public class EmailVerificationTokenService implements IEmailVerificationTokenService{

    @Value("${app.email.verification.token.expiryInSec}")
    private int emailVerificationTokenExpiryInSec;
    @Value("${app.email.verification.token.maxTokenGenerationInSec}")
    private int maxTokenGenerationInSec;

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Override
    @Transactional
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
            token.setAccount(null);
            account.setEmailVerificationToken(null);
            emailVerificationTokenRepository.delete(token);
            emailVerificationTokenRepository.flush();
        }

        var newToken = new EmailVerificationToken(account, emailVerificationTokenExpiryInSec);
        emailVerificationTokenRepository.save(newToken);
        return newToken.getToken();
    }

    @Override
    public String consumeEmailVerificationToken(String token) {
        var emailVerificationToken = emailVerificationTokenRepository.findByToken(token).orElseThrow(
                () -> new EntityNotFoundException("Email verification token not found"));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
        if(emailVerificationToken.isExpired()) {
            throw new EmailVerificationTokenExpired("Email verification token has expired");
        }

        return emailVerificationToken.getAccount().getEmail();
    }

}
