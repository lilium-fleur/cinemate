package com.fleur.cinemate.core.relations.filmPerson;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmPersonRepository extends JpaRepository<FilmPerson, Long> {

    Optional<FilmPerson> findByFilmIdAndPersonIdAndRole(Long filmId, Long actorId, Role role);

    List<FilmPerson> findAllByFilmIdAndRole(Long filmId, Role role);

    Page<FilmPerson> findAllByFilmId(Long filmId, Pageable pageable);

    Page<FilmPerson> findAllByPersonId(Long actorId, Pageable pageable);
}
