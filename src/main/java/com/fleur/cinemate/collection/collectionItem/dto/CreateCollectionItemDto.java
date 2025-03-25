package com.fleur.cinemate.collection.collectionItem.dto;

import jakarta.validation.constraints.NotNull;

public record CreateCollectionItemDto(
        @NotNull
        Long filmId
) {
}
