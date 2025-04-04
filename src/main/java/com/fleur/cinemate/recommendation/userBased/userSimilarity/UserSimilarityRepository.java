package com.fleur.cinemate.recommendation.userBased.userSimilarity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSimilarityRepository extends JpaRepository<UserSimilarity, Long> {

    @Query("SELECT us FROM UserSimilarity us " +
            "WHERE us.user1.id = : user1Id " +
            "AND us.user2.id = :user2Id")
    Optional<UserSimilarity> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    @Query("SELECT us FROM UserSimilarity us " +
            "WHERE us.user1 = :userId " +
            "OR us.user2 = :userId")
    List<UserSimilarity> findByUserId(Long userId);


    @Modifying
    @Query("DELETE FROM UserSimilarity us " +
            "WHERE us.user1 = :userId " +
            "OR us.user2 = :userId")
    void deleteByUserId(Long userId);
}
