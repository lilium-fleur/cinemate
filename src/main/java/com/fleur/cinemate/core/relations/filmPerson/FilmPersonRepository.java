package com.fleur.cinemate.core.relations.filmPerson;

import com.fleur.cinemate.core.relations.filmPerson.model.FilmPerson;
import com.fleur.cinemate.core.relations.filmPerson.model.FilmPersonProjection;
import com.fleur.cinemate.core.relations.filmPerson.model.FilmRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmPersonRepository extends JpaRepository<FilmPerson, Long> {

    Optional<FilmPerson> findByFilmIdAndPersonIdAndFilmRole(Long filmId, Long actorId, FilmRole filmRole);

    @Query("SELECT fp.film.id AS filmId, p.name AS personName " +
            "FROM FilmPerson fp JOIN fp.person p " +
            "WHERE fp.film.id IN :filmIds " +
            "AND fp.filmRole = :filmRole")
    List<FilmPersonProjection> findPersonNamesByFilmIdAndRole(List<Long> filmIds, FilmRole filmRole);

    Page<FilmPerson> findAllByFilmId(Long filmId, Pageable pageable);

    Page<FilmPerson> findAllByPersonId(Long actorId, Pageable pageable);
}
