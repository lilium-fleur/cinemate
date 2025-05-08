package com.fleur.cinemate.search.entity;

import com.fleur.cinemate.__shared.exception.BadRequestException;

public enum IndexName {
    FILMS,
    PERSONS,
    COLLECTIONS;

    public static IndexName fromString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new BadRequestException("Enum value cannot be null or empty");
        }
        for (IndexName indexName : IndexName.values()) {
            if (indexName.toString().equals(string.toUpperCase())) {
                return indexName;
            }
        }
        throw new BadRequestException("Invalid index name");
    }

}
