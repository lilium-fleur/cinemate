package com.fleur.cinemate.core.ralations.filmGenre.dto;

import jakarta.validation.constraints.NotNull;

public record CreateFilmGenreDto(
        @NotNull
        Long genreId
) {

}
