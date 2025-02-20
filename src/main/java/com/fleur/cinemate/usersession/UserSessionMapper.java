package com.fleur.cinemate.usersession;

import com.fleur.cinemate.usersession.dto.UserSessionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {

    UserSessionDto toDto(UserSession userSession);
}
