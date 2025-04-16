package com.fleur.cinemate.core.person;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT p FROM Person p " +
            "WHERE p.createdAt > :sinceDate " +
            "OR p.lastModifiedAt > :sinceDate")
    Page<Person> findModifiedSince(Instant sinceDate, Pageable pageable);
}
