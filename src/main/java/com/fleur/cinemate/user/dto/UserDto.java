package com.fleur.cinemate.user.dto;

import java.time.Instant;

public record UserDto(
        Long id,
        String username,
        String email,
        Instant createdAt
) {
}
