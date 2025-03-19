package com.fleur.cinemate.core.genre.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateGenreDto(
        @NotBlank
        String name
) {
}
