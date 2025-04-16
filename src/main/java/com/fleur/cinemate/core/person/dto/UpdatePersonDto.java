package com.fleur.cinemate.core.person.dto;

import java.time.LocalDate;

public record UpdatePersonDto(
        String name,
        LocalDate born,
        String portraitUrl
) {
}
