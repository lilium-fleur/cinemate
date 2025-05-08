package com.fleur.cinemate.recommendation.dto;

import com.fleur.cinemate.core.film.Film;
import lombok.Builder;

@Builder
public record RecommendationDto(
        Film film,
        Double recommendationScore
) {
}
