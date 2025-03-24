package com.fleur.cinemate.auth.dto;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.user.session.UserSession;
import lombok.Builder;

@Builder
public record CreateTokenDto(
        UserSession userSession,
        User user,
        String token
) {
}
