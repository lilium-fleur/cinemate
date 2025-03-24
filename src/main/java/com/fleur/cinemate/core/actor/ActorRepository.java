package com.fleur.cinemate.core.actor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    @Query("SELECT ac FROM Actor ac " +
            "WHERE ac.createdAt > :sinceDate " +
            "OR ac.lastModifiedAt > :sinceDate")
    Page<Actor> findModifiedSince(Instant sinceDate, Pageable pageable);
}
