package com.fleur.cinemate.filmGenre;

import com.fleur.cinemate.film.Film;
import com.fleur.cinemate.genre.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmGenreRepository extends JpaRepository<FilmGenre, Long> {

    Optional<FilmGenre> findByFilmIdAndGenreId(Long filmId, Long genreId);

    Page<FilmGenre> findByGenre(Genre genre, Pageable pageable);

    Page<FilmGenre> findByFilm(Film film, Pageable pageable);
}
