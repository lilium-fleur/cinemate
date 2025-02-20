package com.fleur.cinemate.user;

import com.fleur.cinemate.user.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
}
