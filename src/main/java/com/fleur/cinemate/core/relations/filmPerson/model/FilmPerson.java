package com.fleur.cinemate.core.relations.filmPerson.model;


import com.fleur.cinemate.__shared.model.IdEntity;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.person.Person;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "film_persons",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"film_id", "person_id", "film_role"}
        )
)
@Entity
public class FilmPerson extends IdEntity {
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FilmRole filmRole;

    @Column(name = "character_name")
    private String characterName;
}
