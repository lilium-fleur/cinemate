package com.fleur.cinemate.core.relations.filmPerson.dto;

public record FilmPersonDto(
        Long id,
        Long filmId,
        Long personId,
        String role,
        String characterName
) {
}
