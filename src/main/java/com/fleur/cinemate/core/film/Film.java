package com.fleur.cinemate.core.film;

import com.fleur.cinemate.__shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @Enumerated(EnumType.STRING)
    private FilmStatus status;

}
