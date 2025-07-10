package com.fleur.cinemate.core.relations.filmGenre;

import com.fleur.cinemate.core.relations.filmGenre.model.FilmGenre;
import com.fleur.cinemate.core.relations.filmGenre.model.FilmGenreId;
import com.fleur.cinemate.core.relations.filmGenre.model.FilmGenreProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmGenreRepository extends JpaRepository<FilmGenre, FilmGenreId> {

    Optional<FilmGenre> findByFilmIdAndGenreId(Long filmId, Long genreId);

    @Query("SELECT fg.film.id AS filmId, g.name AS genreName " +
            "FROM FilmGenre fg JOIN fg.genre g " +
            "WHERE fg.film.id IN :filmIds")
    List<FilmGenreProjection> findGenresByFilmIds(List<Long> filmIds);


    @Query("SELECT g.name AS genreName " +
            "FROM FilmGenre fg JOIN fg.genre g " +
            "WHERE fg.film.id = :filmId")
    List<String> findByFilmId(Long filmId);

    @Query("SELECT fg.film.id AS filmId, g.name AS genreName " +
            "FROM FilmGenre fg JOIN fg.genre g " +
            "WHERE fg.film.id IN :filmIds")
    List<FilmGenreProjection> findByFilmIds(List<Long> filmIds);
}
