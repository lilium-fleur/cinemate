package com.fleur.cinemate.core.relations.filmPerson.model;

import com.fleur.cinemate.__shared.exception.BadRequestException;

public enum FilmRole {
    ACTOR,
    DIRECTOR;


    public static FilmRole fromString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new BadRequestException("Enum value cannot be null or empty");
        }
        for (FilmRole role : FilmRole.values()) {
            if (role.toString().equals(string.toUpperCase())) {
                return role;
            }
        }
        throw new BadRequestException("Invalid FilmRole: " + string);
    }
}
