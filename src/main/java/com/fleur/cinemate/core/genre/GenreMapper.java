package com.fleur.cinemate.core.genre;

import com.fleur.cinemate.core.genre.dto.CreateGenreDto;
import com.fleur.cinemate.core.genre.dto.GenreDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreDto toDto(Genre genre);

    Genre toEntity(CreateGenreDto createGenreDto);
}
