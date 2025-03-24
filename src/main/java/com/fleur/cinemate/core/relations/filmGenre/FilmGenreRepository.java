package com.fleur.cinemate.core.relations.filmGenre;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmGenreRepository extends JpaRepository<FilmGenre, Long> {

    Optional<FilmGenre> findByFilmIdAndGenreId(Long filmId, Long genreId);

    Page<FilmGenre> findByGenreId(Long genreId, Pageable pageable);

    Page<FilmGenre> findByFilmId(Long filmId, Pageable pageable);
}
