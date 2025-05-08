package com.fleur.cinemate.core.film;

import com.fleur.cinemate.__shared.exception.BadRequestException;

public enum FilmStatus {
    DEVELOPMENT,
    PRE_PRODUCTION,
    PRODUCTION,
    POST_PRODUCTION,
    COMPLETED,
    RELEASED,
    CANCELLED;


    public static FilmStatus fromString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new BadRequestException("Enum value cannot be null or empty");
        }
        for (FilmStatus filmStatus : FilmStatus.values()) {
            if (filmStatus.toString().equals(string.toUpperCase())) {
                return filmStatus;
            }
        }
        throw new BadRequestException("Invalid FilmStatus: " + string);
    }
}
