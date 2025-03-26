package com.fleur.cinemate.core.film.dto;

import lombok.Builder;

import java.time.Instant;

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
        Double sourceRating,
        String status,
        Instant createdAt,
        Instant lastModifiedAt
) {
}
