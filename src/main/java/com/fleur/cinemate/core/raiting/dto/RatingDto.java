package com.fleur.cinemate.core.raiting.dto;

import java.time.Instant;

public record RatingDto(
        Long id,
        Long userId,
        Long filmId,
        Integer score,
        Instant createdAt,
        Instant lastModifiedAt
) {
}
