package com.fleur.cinemate.actor.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record CreateActorDto(
        @NotBlank
        String firstname,
        @NotBlank
        String lastname,
        @NotBlank
        Date born,
        String portraitUrl
) {
}
