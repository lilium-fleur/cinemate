package com.fleur.cinemate.collection.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record CollectionDtoWithSize(
        Long id,
        String name,
        String description,
        Long authorId,
        Boolean isPublic,
        Instant createdAt,
        Long size
) {
}
