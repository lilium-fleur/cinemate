package com.fleur.cinemate.core.ralations.filmActor;

import com.fleur.cinemate.core.actor.Actor;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.ralations.filmActor.dto.FilmActorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FilmActorMapper {
    @Mapping(target = "filmId", source = "film", qualifiedByName = "toFilmId")
    @Mapping(target = "actorId", source = "actor", qualifiedByName = "toActorId")
    FilmActorDto toDto(FilmActor filmActor);

    @Named("toFilmId")
    default Long toFilmId(Film film){
        return film.getId();
    }
    @Named("toActorId")
    default Long toActorId(Actor actor){
        return actor.getId();
    }
}
