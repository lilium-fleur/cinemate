package com.fleur.cinemate.collection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCollectionDto(
        @NotBlank
        String name,
        String description,
        @NotNull
        Boolean isPublic
) {
}
