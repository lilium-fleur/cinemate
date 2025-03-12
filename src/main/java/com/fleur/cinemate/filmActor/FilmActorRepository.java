package com.fleur.cinemate.filmActor;

import com.fleur.cinemate.actor.Actor;
import com.fleur.cinemate.film.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmActorRepository  extends JpaRepository<FilmActor, Long> {

    Optional<FilmActor> findByFilmAndActor(Film film, Actor actor);

    Page<FilmActor> findByFilm(Film film, Pageable pageable);

    Page<FilmActor> findByActor(Actor actor, Pageable pageable);
}
