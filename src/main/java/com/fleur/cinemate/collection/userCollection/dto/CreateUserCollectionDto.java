package com.fleur.cinemate.collection.userCollection.dto;

import lombok.Builder;

@Builder
public record CreateUserCollectionDto(
        Long collectionId
) {
}
