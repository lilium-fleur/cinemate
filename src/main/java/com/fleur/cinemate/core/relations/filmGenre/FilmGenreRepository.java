package com.fleur.cinemate.core.relations.filmGenre;

import com.fleur.cinemate.core.relations.filmGenre.model.FilmGenre;
import com.fleur.cinemate.core.relations.filmGenre.model.FilmGenreId;
import com.fleur.cinemate.core.relations.filmGenre.model.FilmGenreProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmGenreRepository extends JpaRepository<FilmGenre, FilmGenreId> {

    Optional<FilmGenre> findByFilmIdAndGenreId(Long filmId, Long genreId);

    Page<FilmGenre> findByGenreId(Long genreId, Pageable pageable);

    Page<FilmGenre> findByFilmId(Long filmId, Pageable pageable);

    @Query("SELECT fg.film.id AS filmId, g.name AS genreName " +
            "FROM FilmGenre fg JOIN fg.genre g " +
            "WHERE fg.film.id IN :filmIds")
    List<FilmGenreProjection> findByFilmIds(List<Long> filmIds);
}
