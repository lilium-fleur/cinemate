package com.fleur.cinemate.userCollection.collection.filmCollectionItem;

import com.fleur.cinemate.userCollection.collection.filmCollectionItem.dto.FilmCollectionItemDto;
import org.mapstruct.Mapper;

@Mapper
public interface FilmCollectionItemMapper {
    FilmCollectionItemDto toDto(FilmCollectionItem filmCollectionItem);
}
