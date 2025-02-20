package com.fleur.cinemate.token.shared.dto;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.usersession.UserSession;
import lombok.Builder;

@Builder
public record CreateTokenDto(
        UserSession userSession,
        User user,
        String token
) {
}
