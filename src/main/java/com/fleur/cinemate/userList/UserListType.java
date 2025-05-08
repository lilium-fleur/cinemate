package com.fleur.cinemate.userList;

import com.fleur.cinemate.__shared.exception.BadRequestException;

public enum UserListType {
    WATCHED,
    WATCHLIST,
    FAVORITE;

    public static UserListType fromString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new BadRequestException("Enum value cannot be null or empty");
        }
        for (UserListType userListType : UserListType.values()) {
            if (userListType.toString().equals(string.toUpperCase())) {
                return userListType;
            }
        }
        throw new BadRequestException("Invalid UserListType: " + string);
    }
}
