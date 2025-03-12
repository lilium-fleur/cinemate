package com.fleur.cinemate.filmActor;


import com.fleur.cinemate.__shared.model.IdEntity;
import com.fleur.cinemate.actor.Actor;
import com.fleur.cinemate.film.Film;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "film_actors",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"film_id", "actor_id"}
        )
)
@Entity
@IdClass(FilmActorId.class)
public class FilmActor{

        @Id
        @ManyToOne
        @JoinColumn(name = "film_id", nullable = false)
        private Film film;

        @Id
        @ManyToOne
        @JoinColumn(name = "actor_id", nullable = false)
        private Actor actor;
}
