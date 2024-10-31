package com.ics.events.publishers.emails;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailVerificationTokenGeneratedEvent extends ApplicationEvent{
    private final String token;
    private final String email;

    public EmailVerificationTokenGeneratedEvent(Object source, String token, String email) {
        super(source);
        this.token = token;
        this.email = email;
    }
}
