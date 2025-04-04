package com.fleur.cinemate.collection.collectionItem;


import com.fleur.cinemate.__shared.model.IdEntity;
import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.core.film.Film;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "collection_items",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"collection_id", "film_id"}
        ))
public class CollectionItem extends IdEntity {

    @JoinColumn(name = "collection_id", nullable = false)
    @ManyToOne
    private Collection collection;

    @JoinColumn(name = "film_id", nullable = false)
    @ManyToOne
    private Film film;

    @Column(nullable = false)
    @Builder.Default
    private Instant addedAt = Instant.now();

    @Column(nullable = false)
    private Integer position;
}
