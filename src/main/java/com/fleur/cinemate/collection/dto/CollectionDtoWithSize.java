package com.fleur.cinemate.collection.dto;

import java.time.Instant;

public interface CollectionDtoWithSize {
    Long getId();

    String getName();

    String getDescription();

    Long getAuthorId();

    Boolean getIsPublic();

    Instant getCreatedAt();

    Long getSize();

}
