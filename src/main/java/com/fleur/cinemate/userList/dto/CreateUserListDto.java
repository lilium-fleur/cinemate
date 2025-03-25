package com.fleur.cinemate.userList.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserListDto(
        @NotNull
        Long filmId,
        @NotBlank
        String type
) {
}
