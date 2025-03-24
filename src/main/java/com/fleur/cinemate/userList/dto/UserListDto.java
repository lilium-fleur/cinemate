package com.fleur.cinemate.userList.dto;

import com.fleur.cinemate.userList.UserListType;

import java.util.Date;

public record UserListDto(
        Long id,
        Long userId,
        Long filmId,
        UserListType type,
        Date addedAt
) {
}
