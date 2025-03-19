package com.fleur.cinemate.userCollection.collection.collection.dto;

public record UpdateCollectionDto(
        String name,
        String description,
        Boolean isPublic
) {
}
