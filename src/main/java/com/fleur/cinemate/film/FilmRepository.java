package com.fleur.cinemate.film;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    Page<Film> findAll(Pageable pageable);

    @Query(
            value = """
                    SELECT *, 
                    ts_rank(to_tsvector('russian', title), to_tsquery('russian', :title)) AS rank
                    FROM films
                    WHERE to_tsvector('russian', title) @@ to_tsquery('russian', :title)
                    ORDER BY rank DESC
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM films
                    WHERE to_tsvector('russian', title) @@ to_tsquery('russian', :title)
                    """,
            nativeQuery = true
    )
    Page<Film> findByTitle(String title, Pageable pageable);

}
