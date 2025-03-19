package com.fleur.cinemate.core.ralations.filmActor;


import com.fleur.cinemate.core.actor.Actor;
import com.fleur.cinemate.core.film.Film;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
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
