package com.ics.events.publishers.emails;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PasswordHaveBeenResetedEvent extends ApplicationEvent {
    private final String email;

    public PasswordHaveBeenResetedEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
