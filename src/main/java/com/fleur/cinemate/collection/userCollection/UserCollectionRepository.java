package com.fleur.cinemate.collection.userCollection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserCollectionRepository extends JpaRepository<UserCollection, UserCollectionId> {
    Page<UserCollection> findAllByUserId(Long userId, Pageable pageable);

    Optional<UserCollection> findByUserIdAndCollectionId(Long userId, Long collectionId);

    @Query("SELECT uc FROM UserCollection uc " +
            "WHERE uc.collection.isPublic = true")
    Page<UserCollection> findPublicCollectionsByUserId(Long userId, Pageable pageable);
}
