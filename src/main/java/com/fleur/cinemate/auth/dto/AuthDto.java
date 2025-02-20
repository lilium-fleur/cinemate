package com.fleur.cinemate.auth.dto;

import com.fleur.cinemate.user.dto.UserDto;
import lombok.Builder;

@Builder
public record AuthDto(
        UserDto userDto,
        String token

) {
}
