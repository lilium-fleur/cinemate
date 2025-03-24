package com.fleur.cinemate.core.relations.filmActor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmActorRepository  extends JpaRepository<FilmActor, Long> {

    Optional<FilmActor> findByFilmIdAndActorId(Long filmId, Long actorId);

    Page<FilmActor> findByFilmId(Long filmId, Pageable pageable);

    Page<FilmActor> findByActorId(Long actorId, Pageable pageable);
}
