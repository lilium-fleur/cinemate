package com.fleur.cinemate.userCollection.userList.dto;

import java.util.Date;

public record UserListDto(
        Long id,
        Long userId,
        Long filmId,
        String type,
        Date addedAt
) {
}
