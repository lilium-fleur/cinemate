package com.fleur.cinemate.collection.item;

import com.fleur.cinemate.collection.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CollectionItemRepository extends JpaRepository<CollectionItem, Long> {

    @Query("SELECT MAX(ci.position) FROM CollectionItem ci " +
            "WHERE ci.collection = :collection")
    Integer findMaxPositionByFilmCollection(Collection collection);

    @Modifying
    @Query("UPDATE CollectionItem ci SET ci.position = ci.position - 1 " +
            "WHERE ci.collection = :collection " +
            "AND ci.position BETWEEN :start AND :end")
    void shiftPositionDownByFilmCollection(Collection collection, Integer start, Integer end);

    @Modifying
    @Query("UPDATE CollectionItem ci SET ci.position = ci.position + 1 " +
            "WHERE ci.collection = :collection " +
            "AND ci.position BETWEEN :start AND :end")
    void shiftPositionUpByFilmCollection(Collection collection, Integer start, Integer end);

    Page<CollectionItem> findByCollectionIdOrderByPosition(Long collectionId, Pageable pageable);


    Optional<CollectionItem> findByCollectionIdAndFilmId(Long collectionId, Long filmId);
}
