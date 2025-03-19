package com.fleur.cinemate.core.film;

import com.fleur.cinemate.core.film.dto.CreateFilmDto;
import com.fleur.cinemate.core.film.dto.FilmDto;
import com.fleur.cinemate.core.film.dto.UpdateFilmDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FilmMapper {

    FilmDto toDto(Film film);

    void updateEntityFromDto(UpdateFilmDto updateFilmDto, @MappingTarget Film film);

    Film toEntity(CreateFilmDto createFilmDto);
}
