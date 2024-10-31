package com.ics.events.publishers;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountBecomeMemberEvent extends ApplicationEvent {
    private final String email;

    public AccountBecomeMemberEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
