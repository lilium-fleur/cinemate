package com.fleur.cinemate.actor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    @Query("SELECT a FROM Actor a JOIN a.films f " +
            "WHERE f.title = :filmTitle")
    Page<Actor> findByFilmTitle(String filmTitle, Pageable pageable);

}
