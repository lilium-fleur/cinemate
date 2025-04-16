package com.fleur.cinemate.core.person.dto;

import java.time.Instant;
import java.time.LocalDate;

public record PersonDto(
        Long id,
        String name,
        LocalDate born,
        String portraitUrl,
        Instant createdAt,
        Instant lastModifiedAt
) {
}
