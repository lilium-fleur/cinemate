package com.fleur.cinemate.collection.dto;

public record UpdateCollectionDto(
        String name,
        String description,
        Boolean isPublic
) {
}
