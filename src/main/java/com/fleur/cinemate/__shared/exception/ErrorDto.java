package com.fleur.cinemate.__shared.exception;

import lombok.Builder;

@Builder
public record ErrorDto(
        int statusCode,
        String message,
        String description
) {
}
