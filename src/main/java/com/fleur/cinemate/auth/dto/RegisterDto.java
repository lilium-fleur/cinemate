package com.fleur.cinemate.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDto(
        @NotBlank
        @Size(min = 3)
        String username,
        @NotBlank
        @Size(min = 6)
        String email,
        @NotBlank
        @Size(min = 6)
        String password,
        @NotBlank
        String fingerprint
) {
}
