package com.fleur.cinemate.collection;

import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.collection.dto.UpdateCollectionDto;
import com.fleur.cinemate.user.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CollectionMapper {

    @Mapping(target = "authorId", source = "author", qualifiedByName = "toAuthorId")
    CollectionDto toDto(Collection collection);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    Collection updateEntityFromDto(UpdateCollectionDto updateCollectionDto,
                                   @MappingTarget Collection collection);

    @Named("toAuthorId")
    default Long toAuthorId(User user) {
        return user.getId();
    }
}
