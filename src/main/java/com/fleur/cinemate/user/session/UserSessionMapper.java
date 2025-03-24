package com.fleur.cinemate.user.session;

import com.fleur.cinemate.user.dto.UserSessionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {

    UserSessionDto toDto(UserSession userSession);
}
