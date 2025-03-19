package com.fleur.cinemate.userCollection.collection.collectionItem;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.userCollection.collection.collection.Collection;
import com.fleur.cinemate.userCollection.collection.collectionItem.dto.CollectionItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CollectionItemMapper {
    @Mapping(target = "collectionId", source = "collection", qualifiedByName = "toCollectionId")
    @Mapping(target = "filmId", source = "film", qualifiedByName = "toFilmId")
    CollectionItemDto toDto(CollectionItem collectionItem);

    @Named("toCollectionId")
    default Long toCollectionId(Collection collection){
        return collection.getId();
    }

    @Named("toFilmId")
    default Long toFilmId(Film film){
        return film.getId();
    }
}
