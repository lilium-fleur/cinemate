package com.fleur.cinemate.core.actor.dto;

import java.time.LocalDate;

public record UpdateActorDto(
        String name,
        LocalDate born,
        String portraitUrl
) {
}
