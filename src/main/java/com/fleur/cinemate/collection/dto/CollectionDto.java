package com.fleur.cinemate.collection.dto;

public record CollectionDto(
        Long id,
        String name,
        String description,
        Long authorId,
        Boolean isPublic
) {
}
