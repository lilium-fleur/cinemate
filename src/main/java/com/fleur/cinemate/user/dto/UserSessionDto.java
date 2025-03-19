package com.fleur.cinemate.user.dto;

import java.time.Instant;

public record UserSessionDto(
        Long id,
        String ipAddress,
        String deviceInfo,
        Instant lastActivity,
        Boolean isActive,
        Instant createdAt
) {
}
