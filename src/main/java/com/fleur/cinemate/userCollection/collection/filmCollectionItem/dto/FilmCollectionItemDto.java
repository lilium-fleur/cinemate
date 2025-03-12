package com.fleur.cinemate.userCollection.collection.filmCollectionItem.dto;

import java.time.Instant;

public record FilmCollectionItemDto(
        Long filmCollectionId,
        Long filmId,
        Instant addedAt,
        Integer position
) {
}
