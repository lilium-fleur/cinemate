package com.fleur.cinemate.core.genre.dto;

import java.time.Instant;

public record GenreDto(
        Long id,
        String name,
        Instant createdAt,
        Instant lastModifiedAt
) {
}
