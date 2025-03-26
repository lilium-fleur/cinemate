package com.fleur.cinemate.core.film.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateFilmDto(
        @NotBlank
        String title,
        @NotBlank
        String description,
        Integer releaseYear,
        Integer duration,
        String ageRating,
        String trailerUrl,
        String posterUrl,
        Double sourceRating,
        String status
) {
}
