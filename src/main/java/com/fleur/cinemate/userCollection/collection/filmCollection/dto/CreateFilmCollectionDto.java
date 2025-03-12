package com.fleur.cinemate.userCollection.collection.filmCollection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFilmCollectionDto(
        @NotBlank
        String name,

        String description,

        @NotNull
        Boolean isPublic
) {
}
