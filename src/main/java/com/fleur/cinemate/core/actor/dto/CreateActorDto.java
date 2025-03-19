package com.fleur.cinemate.core.actor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public record CreateActorDto(
        @NotBlank
        String name,
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate born,
        String portraitUrl
) {
}
