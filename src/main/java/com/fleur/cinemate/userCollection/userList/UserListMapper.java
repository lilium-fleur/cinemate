package com.fleur.cinemate.userCollection.userList;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userCollection.userList.dto.CreateUserListDto;
import com.fleur.cinemate.userCollection.userList.dto.UserListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserListMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserId")
    @Mapping(target = "filmId", source = "film", qualifiedByName = "toFilmId")
    UserListDto toDto(UserList userList);

    UserList toEntity(CreateUserListDto createUserListDto);

    @Named("toUserId")
    default Long toUseId(User user){
        return user.getId();
    }

    @Named("toFilmId")
    default Long toFilmId(Film film){
        return film.getId();
    }
}
