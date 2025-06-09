package com.fleur.cinemate.collection;

import com.fleur.cinemate.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    Page<Collection> findAllByAuthor(User user, Pageable pageable);

    @Query("SELECT c FROM Collection c " +
            "WHERE c.author.id = :userId " +
            "AND c.isPublic = true")
    Page<Collection> findAllPublicByUserId(Long userId, Pageable pageable);

    @Query("SELECT c FROM Collection c " +
            "WHERE c.id = :collectionId " +
            "AND c.isPublic = true")
    Optional<Collection> findPublicById(Long collectionId);

    @Query("SELECT c FROM Collection c " +
            "WHERE c.isPublic = true")
    Page<Collection> findAllPublic(Pageable pageable);

    @Query("SELECT c FROM Collection c " +
            "WHERE c.isPublic = true " +
            "AND (c.createdAt > :sinceDate OR c.lastModifiedAt > :sinceDate)")
    Page<Collection> findModifiedSince(Instant sinceDate, Pageable pageable);

    @Query("SELECT c.id FROM Collection c " +
            "WHERE c.author.id = :userId")
    List<Long> findCollectionIdsByUser(Long userId);
}
