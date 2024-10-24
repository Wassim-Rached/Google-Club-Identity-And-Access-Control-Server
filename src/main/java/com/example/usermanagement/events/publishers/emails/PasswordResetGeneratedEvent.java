package com.example.usermanagement.events.publishers.emails;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PasswordResetGeneratedEvent extends ApplicationEvent {
    private final String email;
    private final String token;

    public PasswordResetGeneratedEvent(Object source, String email, String token) {
        super(source);
        this.email = email;
        this.token = token;
    }
}
