package com.fleur.cinemate.userCollection.collection.filmCollectionItem;

import com.fleur.cinemate.film.Film;
import com.fleur.cinemate.userCollection.collection.filmCollection.FilmCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FilmCollectionItemRepository extends JpaRepository<FilmCollectionItem, Long> {

    @Query("SELECT MAX(fci.position) FROM FilmCollectionItem fci " +
            "WHERE fci.filmCollection = :filmCollection")
    Integer findMaxPositionByFilmCollection(FilmCollection filmCollection);

    @Modifying
    @Query("UPDATE FilmCollectionItem fci SET fci.position = fci.position - 1 " +
            "WHERE fci.filmCollection = :filmCollection " +
            "AND fci.position BETWEEN :start AND :end")
    void shiftPositionDownByFilmCollection(FilmCollection filmCollection, Integer start, Integer end);

    @Modifying
    @Query("UPDATE FilmCollectionItem fci SET fci.position = fci.position + 1 " +
            "WHERE fci.filmCollection = :filmCollection " +
            "AND fci.position BETWEEN :start AND :end")
    void shiftPositionUpByFilmCollection(FilmCollection filmCollection, Integer start, Integer end);

    Page<FilmCollectionItem> findByFilmCollectionIdOrderByPosition(Long filmCollectionId, Pageable pageable);


    Optional<FilmCollectionItem> findByFilmCollectionAndFilm(FilmCollection filmCollection, Film film);
}
