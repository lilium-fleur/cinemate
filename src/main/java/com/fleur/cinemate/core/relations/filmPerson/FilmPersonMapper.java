package com.fleur.cinemate.core.relations.filmPerson;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.person.Person;
import com.fleur.cinemate.core.relations.filmPerson.dto.FilmPersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FilmPersonMapper {
    @Mapping(target = "filmId", source = "film", qualifiedByName = "toFilmId")
    @Mapping(target = "personId", source = "person", qualifiedByName = "toPersonId")
    FilmPersonDto toDto(FilmPerson filmPerson);

    @Named("toFilmId")
    default Long toFilmId(Film film){
        return film.getId();
    }

    @Named("toPersonId")
    default Long toPersonId(Person person) {
        return person.getId();
    }
}
