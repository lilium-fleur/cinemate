package com.fleur.cinemate.collection.item.dto;

import jakarta.validation.constraints.NotNull;

public record CreateCollectionItemDto(
        @NotNull
        Long filmId
) {
}
