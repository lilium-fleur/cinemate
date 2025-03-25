package com.fleur.cinemate.core.rating.dto;

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
