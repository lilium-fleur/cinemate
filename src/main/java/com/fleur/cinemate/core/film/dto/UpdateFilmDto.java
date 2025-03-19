package com.fleur.cinemate.core.film.dto;

public record UpdateFilmDto(
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
