package com.fleur.cinemate.userCollection.userList.dto;

import com.fleur.cinemate.userCollection.userList.UserListType;

import java.util.Date;

public record UserListDto(
        Long id,
        Long userId,
        Long filmId,
        UserListType type,
        Date addedAt
) {
}
