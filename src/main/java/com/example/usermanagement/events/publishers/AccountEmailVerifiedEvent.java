package com.example.usermanagement.events.publishers;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountEmailVerifiedEvent extends ApplicationEvent {
    private final String email;

    public AccountEmailVerifiedEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
