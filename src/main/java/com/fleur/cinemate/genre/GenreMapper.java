package com.fleur.cinemate.genre;

import com.fleur.cinemate.genre.dto.CreateGenreDto;
import com.fleur.cinemate.genre.dto.GenreDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreDto toDto(Genre genre);

    Genre toEntity(CreateGenreDto createGenreDto);
}
