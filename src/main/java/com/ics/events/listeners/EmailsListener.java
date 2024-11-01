package com.ics.events.listeners;


import com.ics.events.publishers.emails.*;
import com.ics.interfaces.services.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// TODO: make it use thymeleaf for email templates

@Component
@RequiredArgsConstructor
public class EmailsListener {

    @Value("${app.clients.web.amwc}")
    private String amwc;

    private final IEmailService emailService;

    @EventListener(AccountCreatedEvent.class)
    public void onAccountCreatedEvent(AccountCreatedEvent event) {
        String email = event.getEmail();
        String body = "Hello, " + email + "\n" +
                "Your account have been created successfully." + "\n" +
                "If you didn't request this, please contact us.";

        emailService.sendEmail("wa55death405@gmail.com", "Account created", body);
    }

    @EventListener(EmailVerificationTokenGeneratedEvent.class)
    public void onEmailVerificationTokenGeneratedEvent(EmailVerificationTokenGeneratedEvent event) {
        String link = amwc + "/auth/email-verification/confirm?token=" + event.getToken();
        String email = event.getEmail();
        String body = "Hello, " + email + "\n" +
                "Click here to verify your email: " + link + "\n" +
                "If you didn't request this, please ignore this email.";

        emailService.sendEmail("wa55death405@gmail.com", "Email verification", body);
    }

    @EventListener(PasswordResetGeneratedEvent.class)
    public void onPasswordResetGeneratedEvent(PasswordResetGeneratedEvent event) {
        String link = amwc + "/auth/reset-password/confirm?token=" + event.getToken();
        String email = event.getEmail();
        String body = "Hello, " + email + "\n" +
                "Click here to reset your password: " + link + "\n" +
                "If you didn't request this, please ignore this email.";

        emailService.sendEmail("wa55death405@gmail.com", "Password reset", body);
    }

    @EventListener(PasswordHaveBeenResetedEvent.class)
    public void onPasswordHaveBeenResetedEvent(PasswordHaveBeenResetedEvent event) {
        String email = event.getEmail();
        String body = "Hello, " + email + "\n" +
                "Your password have been reseted successfully." + "\n" +
                "If you didn't request this, please contact us.";

        emailService.sendEmail("wa55death405@gmail.com", "Password reseted", body);
    }

    @EventListener(PasswordChangedEvent.class)
    public void onPasswordChangedEvent(PasswordChangedEvent event) {
        String email = event.getEmail();
        String body = "Hello, " + email + "\n" +
                "Your password have been changed successfully." + "\n" +
                "If you didn't request this, please contact us.";

        emailService.sendEmail("wa55death405@gmail.com", "Password changed", body);
    }

    @EventListener(AccountDeletedEvent.class)
    public void onAccountDeletedEvent(AccountDeletedEvent event) {
        String email = event.getEmail();
        String body = "Hello, " + email + "\n" +
                "Your account have been deleted successfully." + "\n" +
                "If you didn't request this, please contact us.";

        emailService.sendEmail("wa55death405@gmail.com", "Account deleted", body);
    }

}
