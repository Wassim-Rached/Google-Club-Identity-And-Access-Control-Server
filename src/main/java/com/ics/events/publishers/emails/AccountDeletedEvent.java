package com.ics.events.publishers.emails;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountDeletedEvent extends ApplicationEvent {
    private final String email;

    public AccountDeletedEvent(Object source, String email) {
        super(source);
        this.email = email;
    }

}
