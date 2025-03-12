package com.fleur.cinemate.film;

import com.fleur.cinemate.__shared.model.BaseEntity;
import com.fleur.cinemate.actor.Actor;
import com.fleur.cinemate.genre.Genre;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "films")
@Entity
public class Film extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private Integer releaseYear;
    private Integer duration;
    private String ageRating;
    private String trailerUrl;
    private String posterUrl;

    @Enumerated(EnumType.STRING)
    private FilmStatus status;

}
