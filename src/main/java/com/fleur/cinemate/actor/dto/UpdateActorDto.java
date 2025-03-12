package com.fleur.cinemate.actor.dto;

import java.util.Date;

public record UpdateActorDto(
        String firstname,
        String lastname,
        Date born,
        String portraitUrl
) {
}
