package com.fleur.cinemate.collection.userCollection;

import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.collection.userCollection.dto.UserCollectionDto;
import com.fleur.cinemate.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserCollectionMapper {
    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserId")
    @Mapping(target = "collectionId", source = "collection", qualifiedByName = "toCollectionId")
    UserCollectionDto toDto(UserCollection userCollection);

    @Named("toUserId")
    default Long toUserId(User user) {
        return user.getId();
    }

    @Named("toCollectionId")
    default Long toCollectionId(Collection collection) {
        return collection.getId();
    }

}
