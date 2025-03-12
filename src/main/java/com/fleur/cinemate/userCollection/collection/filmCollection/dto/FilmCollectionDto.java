package com.fleur.cinemate.userCollection.collection.filmCollection.dto;

import java.time.Instant;

public record FilmCollectionDto(
        Long id,
        String name,
        String description,
        Long userId,
        Boolean isPublic,
        Instant createdAt,
        Instant lastModifiedAt
) {
}
