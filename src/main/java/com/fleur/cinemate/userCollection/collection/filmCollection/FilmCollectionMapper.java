package com.fleur.cinemate.userCollection.collection.filmCollection;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userCollection.collection.filmCollection.dto.FilmCollectionDto;
import com.fleur.cinemate.userCollection.collection.filmCollection.dto.UpdateFilmCollectionDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FilmCollectionMapper {

    @Mapping(target = "userId", source = "user",  qualifiedByName = "toUserId")
    FilmCollectionDto toDto(FilmCollection filmCollection);

    FilmCollection updateEntityFromDto(UpdateFilmCollectionDto updateFilmCollectionDto,
                                       @MappingTarget FilmCollection filmCollection);

    @Named("toUserId")
    default Long toUserId(User user){
        return user.getId();
    }
}
