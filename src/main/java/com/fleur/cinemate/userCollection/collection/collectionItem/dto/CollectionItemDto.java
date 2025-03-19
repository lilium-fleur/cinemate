package com.fleur.cinemate.userCollection.collection.collectionItem.dto;

import java.time.Instant;

public record CollectionItemDto(
        Long id,
        Long collectionId,
        Long filmId,
        Instant addedAt,
        Integer position
) {
}
