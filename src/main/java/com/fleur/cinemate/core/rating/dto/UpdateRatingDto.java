package com.fleur.cinemate.core.rating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateRatingDto(
        @NotNull
        Long filmId,
        @Min(0)
        @Max(10)
        Double rating
) {
}
