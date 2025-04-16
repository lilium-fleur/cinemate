package com.fleur.cinemate.core.relations.filmPerson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFilmPersonDto(
        @NotNull
        Long personId,
        @NotBlank
        String role,
        String characterName
) {
}
