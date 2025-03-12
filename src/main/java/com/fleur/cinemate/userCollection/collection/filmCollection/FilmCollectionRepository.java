package com.fleur.cinemate.userCollection.collection.filmCollection;

import com.fleur.cinemate.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmCollectionRepository extends JpaRepository<FilmCollection, Long> {
    Page<FilmCollection> findAllByUser(User user, Pageable pageable);

    @Query("SELECT fc FROM FilmCollection fc " +
            "WHERE fc.user.id = :userId " +
            "AND fc.isPublic = true")
    Page<FilmCollection> findAllPublicByUserId(Long userId, Pageable pageable);

    @Query("SELECT fc FROM FilmCollection fc " +
            "WHERE fc.isPublic = true")
    Page<FilmCollection> findAllPublic(Pageable pageable);
}
