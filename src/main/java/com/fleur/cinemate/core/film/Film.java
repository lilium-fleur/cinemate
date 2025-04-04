package com.fleur.cinemate.core.film;

import com.fleur.cinemate.__shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


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
    private Double sourceRating;

    @Enumerated(EnumType.STRING)
    private FilmStatus status;

}
