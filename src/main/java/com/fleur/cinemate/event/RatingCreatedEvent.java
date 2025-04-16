package com.fleur.cinemate.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RatingCreatedEvent extends ApplicationEvent {
    private Long user;

    public RatingCreatedEvent(Object source, Long user) {
        super(source);
        this.user = user;
    }
}
