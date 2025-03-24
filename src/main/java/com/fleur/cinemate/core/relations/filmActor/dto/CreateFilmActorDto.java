package com.fleur.cinemate.core.relations.filmActor.dto;

import jakarta.validation.constraints.NotNull;

public record CreateFilmActorDto(
        @NotNull
        Long actorId
) {
}
