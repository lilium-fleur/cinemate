package com.fleur.cinemate.core.ralations.filmActor.dto;

import jakarta.validation.constraints.NotNull;

public record CreateFilmActorDto(
        @NotNull
        Long actorId
) {
}
