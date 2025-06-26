package com.fleur.cinemate.search.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CollectionSortBy {
    CREATED_AT,
    NAME,
    SIZE;

    @JsonCreator
    public static CollectionSortBy fromString(String value) {
        if (value == null) return CollectionSortBy.NAME;

        try {
            return CollectionSortBy.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Illegal value for sorting collections: %s", value));
        }
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
