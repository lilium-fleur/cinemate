package com.fleur.cinemate.userCollection.collection.collection.dto;

import java.time.Instant;

public record CollectionDto(
        Long id,
        String name,
        String description,
        Long userId,
        Boolean isPublic,
        Instant createdAt,
        Instant lastModifiedAt
) {
}
