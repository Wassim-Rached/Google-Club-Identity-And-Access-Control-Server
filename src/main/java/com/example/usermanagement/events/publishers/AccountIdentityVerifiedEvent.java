package com.example.usermanagement.events.publishers;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountIdentityVerifiedEvent extends ApplicationEvent {
    private final String email;

    public AccountIdentityVerifiedEvent(Object source, String email) {
        super(source);
        this.email = email;
    }

}
