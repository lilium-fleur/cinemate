package com.fleur.cinemate.core.relations.filmGenre;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.genre.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "film_genres",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"film_id", "genre_id"}
        )
)
@Entity
@IdClass(FilmGenreId.class)
public class FilmGenre {

    @Id
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @Id
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

}
