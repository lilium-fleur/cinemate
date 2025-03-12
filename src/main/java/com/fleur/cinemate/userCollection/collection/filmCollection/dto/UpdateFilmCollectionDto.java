package com.fleur.cinemate.userCollection.collection.filmCollection.dto;

public record UpdateFilmCollectionDto(
        String name,
        String description,
        Boolean isPublic
) {
}
