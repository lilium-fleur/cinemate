package com.fleur.cinemate.film.dto;

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
