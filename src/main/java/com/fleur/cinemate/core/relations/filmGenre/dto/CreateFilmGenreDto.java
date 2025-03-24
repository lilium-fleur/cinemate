package com.fleur.cinemate.core.relations.filmGenre.dto;

import jakarta.validation.constraints.NotNull;

public record CreateFilmGenreDto(
        @NotNull
        Long genreId
) {

}
