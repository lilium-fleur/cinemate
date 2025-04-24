package com.fleur.cinemate.userList;

public enum UserListType {
    WATCHED,
    WATCHLIST,
    FAVORITE;

    public UserListType fromString(String string) {
        for (UserListType userListType : UserListType.values()) {
            if (userListType.toString().equals(string.toUpperCase())) {
                return userListType;
            }
        }
        throw new IllegalArgumentException("Invalid UserListType: " + string);
    }
}
