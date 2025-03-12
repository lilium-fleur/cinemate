package com.fleur.cinemate.film.dto;

import lombok.Builder;

@Builder
public record FilmDto(
        Long id,
        String title,
        String description,
        Integer releaseYear,
        Integer duration,
        String ageRating,
        String trailerUrl,
        String posterUrl,
        String status
) {
}
