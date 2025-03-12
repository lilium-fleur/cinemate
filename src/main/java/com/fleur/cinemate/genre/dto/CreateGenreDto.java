package com.fleur.cinemate.genre.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateGenreDto(
        @NotBlank
        String name
) {
}
