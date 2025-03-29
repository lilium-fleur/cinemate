package com.fleur.cinemate.search.service.sync;

import com.fleur.cinemate.search.entity.ESSyncDate;
import com.fleur.cinemate.search.entity.IndexName;
import com.fleur.cinemate.search.repository.ESSyncDateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public abstract class SyncService<T> {
    private final ESSyncDateRepository esSyncDateRepository;

    protected SyncService(ESSyncDateRepository esSyncDateRepository) {
        this.esSyncDateRepository = esSyncDateRepository;
    }

    public void syncAll() {
        int page = 0;
        int size = 100;
        Page<T> entityPage;
        do {
            entityPage = findAllEntities(PageRequest.of(page, size));
            saveToIndex(entityPage);
            page++;
        } while (entityPage.hasNext());

        esSyncDateRepository.save(
                ESSyncDate.builder()
                        .lastSyncTime(Instant.now())
                        .indexName(getIndexName())
                        .build());
    }

    public void incrementalSync() {
        int page = 0;
        int size = 100;
        Page<T> entityPage;
        Instant lastSyncTime = esSyncDateRepository.findFirstByIndexNameOrderByLastSyncTime(getIndexName())
                .map(ESSyncDate::getLastSyncTime)
                .orElse(Instant.EPOCH);
        do {
            entityPage = findEntitiesSinceDate(lastSyncTime, PageRequest.of(page, size));
            saveToIndex(entityPage);
            page++;
        } while (entityPage.hasNext());

        esSyncDateRepository.save(
                ESSyncDate.builder()
                        .lastSyncTime(Instant.now())
                        .indexName(getIndexName())
                        .build());
    }

    abstract protected IndexName getIndexName();

    abstract protected Page<T> findAllEntities(Pageable pageable);

    abstract protected Page<T> findEntitiesSinceDate(Instant sinceDate, Pageable pageable);

    abstract protected void saveToIndex(Page<T> entityPage);
}
