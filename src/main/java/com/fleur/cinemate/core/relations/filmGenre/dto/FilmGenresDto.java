package com.fleur.cinemate.core.relations.filmGenre.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record FilmGenresDto(
        Long filmId,
        List<String> genreNames
) {
}
