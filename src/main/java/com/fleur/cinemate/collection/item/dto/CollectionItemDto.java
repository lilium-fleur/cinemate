package com.fleur.cinemate.collection.item.dto;

import java.time.Instant;

public record CollectionItemDto(
        Long id,
        Long collectionId,
        Long filmId,
        Instant addedAt,
        Integer position
) {
}
