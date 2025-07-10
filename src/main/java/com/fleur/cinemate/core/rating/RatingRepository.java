package com.fleur.cinemate.core.rating;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r.film.id AS filmId, r.rating AS rating " +
            "FROM Rating r " +
            "WHERE r.film.id IN :filmIds " +
            "AND r.user.id = :userId")
    List<RatingProjection> findByFilmIdsAndUserId(Set<Long> filmIds, Long userId);

    Optional<Rating> findByFilmIdAndUserId(Long filmId, Long userId);

    List<Rating> findAllByUserId(Long userId);

    Page<Rating> findByFilmIdAndUserIdIn(Long filmId, List<Long> users, Pageable pageable);

    Page<Rating> findByFilmIdNotIn(List<Long> excludeFilmIds, Pageable pageable);

    Page<Rating> findByFilmIdIn(List<Long> filmIds, Pageable pageable);

    Page<Rating> findByUserIdAndFilmIdIn(Long userId, List<Long> filmIds, Pageable pageable);

    @Query("SELECT DISTINCT r.user.id FROM Rating r")
    Page<Long> findDistinctByUserIds(Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Rating r " +
            "WHERE r.user.id = :userId")
    Double findAverageRatingByUserId(Long userId);

}
