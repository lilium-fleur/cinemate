package com.fleur.cinemate.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDto(
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
