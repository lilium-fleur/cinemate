package com.fleur.cinemate.core.film;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;


@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("SELECT f FROM Film f " +
            "WHERE f.createdAt > :sinceDate " +
            "OR f.lastModifiedAt > :sinceDate")
    Page<Film> findModifiedSince(Instant sinceDate, Pageable pageable);

    @Query("SELECT f FROM Film f " +
            "WHERE f.id IN :ids")
    Page<Film> findAllByIds(List<Long> ids, Pageable pageable);

    Page<Film> findByIdNotIn(Set<Long> ids, Pageable pageable);
}
