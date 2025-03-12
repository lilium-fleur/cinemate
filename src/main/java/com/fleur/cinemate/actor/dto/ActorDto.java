package com.fleur.cinemate.actor.dto;

import java.util.Date;

public record ActorDto(
        Long id,
        String firstname,
        String lastname,
        Date born,
        String portraitUrl
) {
}
