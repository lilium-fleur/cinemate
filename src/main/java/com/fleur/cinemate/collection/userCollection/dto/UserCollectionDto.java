package com.fleur.cinemate.collection.userCollection.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record UserCollectionDto(
        Long userId,
        Long collectionId,
        Instant addedAt
) {
}
