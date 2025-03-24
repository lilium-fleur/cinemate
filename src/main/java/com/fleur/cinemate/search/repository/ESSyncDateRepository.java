package com.fleur.cinemate.search.repository;

import com.fleur.cinemate.search.entity.ESSyncDate;
import com.fleur.cinemate.search.entity.IndexName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ESSyncDateRepository extends JpaRepository<ESSyncDate, Long> {

    Optional<ESSyncDate> findFirstByIndexNameOrderByLastSyncTime(IndexName indexName);
}
