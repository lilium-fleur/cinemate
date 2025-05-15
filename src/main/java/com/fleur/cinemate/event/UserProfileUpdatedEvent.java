package com.fleur.cinemate.event;

import com.fleur.cinemate.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserProfileUpdatedEvent extends ApplicationEvent {
    private User user;

    public UserProfileUpdatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
