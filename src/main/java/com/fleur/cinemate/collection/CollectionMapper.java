package com.fleur.cinemate.collection;

import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.collection.dto.UpdateCollectionDto;
import com.fleur.cinemate.user.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CollectionMapper {

    @Mapping(target = "userId", source = "user",  qualifiedByName = "toUserId")
    CollectionDto toDto(Collection collection);

    Collection updateEntityFromDto(UpdateCollectionDto updateCollectionDto,
                                   @MappingTarget Collection collection);

    @Named("toUserId")
    default Long toUserId(User user){
        return user.getId();
    }
}
