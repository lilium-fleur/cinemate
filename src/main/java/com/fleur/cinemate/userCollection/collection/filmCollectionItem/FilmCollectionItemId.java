package com.fleur.cinemate.userCollection.collection.filmCollectionItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmCollectionItemId implements Serializable {

    private Long collectionId;

    private Long filmId;
}
