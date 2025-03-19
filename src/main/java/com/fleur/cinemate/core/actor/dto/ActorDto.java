package com.fleur.cinemate.core.actor.dto;

import java.time.Instant;
import java.time.LocalDate;

public record ActorDto(
        Long id,
        String name,
        LocalDate born,
        String portraitUrl,
        Instant createdAt,
        Instant lastModifiedAt
) {
}
