package com.fleur.cinemate.userCollection.collection.filmCollectionItem;


import com.fleur.cinemate.film.Film;
import com.fleur.cinemate.userCollection.collection.filmCollection.FilmCollection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "film_collection_items",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"collection_id", "film_id"}
        ))
@IdClass(FilmCollectionItemId.class)
public class FilmCollectionItem {

    @Id
    @JoinColumn(name = "film_collection_id", nullable = false)
    @ManyToOne
    private FilmCollection filmCollection;

    @Id
    @JoinColumn(name = "film_id", nullable = false)
    @ManyToOne
    private Film film;

    @Column(nullable = false)
    @Builder.Default
    private Instant addedAt = Instant.now();

    @Column(nullable = false)
    private Integer position;
}
