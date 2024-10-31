package com.ics.events.publishers;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountNoLongerMemberEvent extends ApplicationEvent {
    private final String email;

    public AccountNoLongerMemberEvent(Object source, String email) {
        super(source);
        this.email = email;
    }

}
