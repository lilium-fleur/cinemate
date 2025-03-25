package com.fleur.cinemate.search.dto;


import java.util.List;

public record FilmSearchFilter(
        String query,
        List<String> genres,
        Double minRating,
        Double maxRating,
        Integer minYear,
        Integer maxYear
) {
}
