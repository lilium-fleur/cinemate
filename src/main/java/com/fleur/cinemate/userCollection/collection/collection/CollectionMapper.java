package com.fleur.cinemate.userCollection.collection.collection;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userCollection.collection.collection.dto.CollectionDto;
import com.fleur.cinemate.userCollection.collection.collection.dto.UpdateCollectionDto;
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
