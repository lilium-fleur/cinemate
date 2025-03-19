package com.fleur.cinemate.core.raiting;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.raiting.dto.RatingDto;
import com.fleur.cinemate.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserId")
    @Mapping(target = "filmId", source = "film", qualifiedByName = "toFilmId")
    RatingDto toDto(Rating rating);


    @Named("toUserId")
    default Long toUserId(User user) {
        return user.getId();
    }

    @Named("toFilmId")
    default Long toFilmId(Film film) {
        return film.getId();
    }
}

