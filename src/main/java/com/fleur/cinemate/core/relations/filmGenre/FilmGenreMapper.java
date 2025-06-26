package com.fleur.cinemate.core.relations.filmGenre;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.genre.Genre;
import com.fleur.cinemate.core.relations.filmGenre.dto.FilmGenreDto;
import com.fleur.cinemate.core.relations.filmGenre.model.FilmGenre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FilmGenreMapper {
    @Mapping(target = "filmId", source = "film", qualifiedByName = "toFilmId")
    @Mapping(target = "genreName", source = "genre", qualifiedByName = "toGenreName")
    FilmGenreDto toDto(FilmGenre filmGenre);


    @Named("toFilmId")
    default Long toFilmId(Film film) {
        return film.getId();
    }

    @Named("toGenreName")
    default String toGenreName(Genre genre) {
        return genre.getName();
    }
}
