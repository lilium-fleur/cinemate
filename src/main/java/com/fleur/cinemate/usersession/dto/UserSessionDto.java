package com.fleur.cinemate.usersession.dto;

import java.time.Instant;

public record UserSessionDto(
        Long id,
        String ipAddress,
        String deviceInfo,
        Instant lastActivity,
        Boolean isActive
) {
}
