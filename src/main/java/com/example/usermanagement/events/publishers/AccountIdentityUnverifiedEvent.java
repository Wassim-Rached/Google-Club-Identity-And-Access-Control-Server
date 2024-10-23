package com.example.usermanagement.events.publishers;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountIdentityUnverifiedEvent extends ApplicationEvent {
    private final String email;

    public AccountIdentityUnverifiedEvent(Object source, String email) {
        super(source);
        this.email = email;
    }

}
