package com.fleur.cinemate.search.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FilmSortBy {
    TITLE,
    RATING,
    RELEASE_YEAR;

    @JsonCreator
    public static FilmSortBy fromString(final String value) {
        if (value == null) return FilmSortBy.RATING;
        try {
            return FilmSortBy.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Illegal value for sorting films: %s", value));
        }
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
