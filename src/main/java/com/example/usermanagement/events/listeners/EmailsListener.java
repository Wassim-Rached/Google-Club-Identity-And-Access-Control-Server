package com.example.usermanagement.events.listeners;


import com.example.usermanagement.events.publishers.emails.EmailVerificationTokenGeneratedEvent;
import com.example.usermanagement.events.publishers.emails.PasswordHaveBeenResetedEvent;
import com.example.usermanagement.events.publishers.emails.PasswordResetGeneratedEvent;
import com.example.usermanagement.interfaces.services.IEmailService;
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

    @EventListener(EmailVerificationTokenGeneratedEvent.class)
    public void onEmailVerificationTokenGeneratedEvent(EmailVerificationTokenGeneratedEvent event) {
        String link = amwc + "/api/accounts/verify-email?token=" + event.getToken();
        String body = "Click here to verify your email: " + link;
        emailService.sendEmail("wa55death405@gmail.com", "Email verification", body);
    }

    @EventListener(PasswordResetGeneratedEvent.class)
    public void onPasswordResetGeneratedEvent(PasswordResetGeneratedEvent event) {
        String link = amwc + "/api/accounts/reset-password?token=" + event.getToken();
        String body = "Click here to reset your password: " + link;
        emailService.sendEmail(event.getEmail(), "Password reset", body);
    }

    @EventListener(PasswordHaveBeenResetedEvent.class)
    public void onPasswordHaveBeenResetedEvent(PasswordHaveBeenResetedEvent event) {
        String body = "Your password have been reseted";
        emailService.sendEmail(event.getEmail(), "Password reseted", body);
    }
}
