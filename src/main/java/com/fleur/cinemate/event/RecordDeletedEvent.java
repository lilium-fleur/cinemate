package com.fleur.cinemate.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RecordDeletedEvent extends ApplicationEvent {
    private final Long recordId;
    private final String entityName;

    public RecordDeletedEvent(Object source, Long recordId, String entityName) {
        super(source);
        this.recordId = recordId;
        this.entityName = entityName;
    }




}
