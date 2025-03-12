package com.fleur.cinemate.userCollection.collection.filmCollectionItem.dto;

import jakarta.validation.constraints.NotNull;

public record CreateFilmCollectionItemDto(
        @NotNull
        Long film_id
) {
}
