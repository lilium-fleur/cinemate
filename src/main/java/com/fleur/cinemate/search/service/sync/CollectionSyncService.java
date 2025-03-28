package com.fleur.cinemate.search.service.sync;

import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.collection.CollectionService;
import com.fleur.cinemate.search.document.CollectionDocument;
import com.fleur.cinemate.search.entity.IndexName;
import com.fleur.cinemate.search.repository.CollectionDocumentRepository;
import com.fleur.cinemate.search.repository.ESSyncDateRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Log4j2
@Service
public class CollectionSyncService extends SyncService<Collection> {
    private final CollectionDocumentRepository collectionDocumentRepository;
    private final CollectionService collectionService;

    public CollectionSyncService(ESSyncDateRepository esSyncDateRepository,
                                 CollectionDocumentRepository collectionDocumentRepository,
                                 CollectionService collectionService) {
        super(esSyncDateRepository);
        this.collectionDocumentRepository = collectionDocumentRepository;
        this.collectionService = collectionService;
    }

    @Override
    protected IndexName getIndexName() {
        return IndexName.COLLECTIONS;
    }

    @Override
    protected Page<Collection> findAllEntities(Pageable pageable) {
        return collectionService.findAllCollection(pageable);
    }

    @Override
    protected Page<Collection> findEntitiesSinceDate(Instant sinceDate, Pageable pageable) {
        return collectionService.findModifiedSince(sinceDate, pageable);
    }

    @Override
    protected void saveToIndex(Page<Collection> entityPage) {
        for (Collection collection : entityPage) {
            try {
                collectionDocumentRepository.save(convertToDocument(collection));
            } catch (Exception e) {
                log.error("Error saving collection document with id: {} to index: {}",
                        collection.getId(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public CollectionDocument convertToDocument(Collection collection) {
        return CollectionDocument.builder()
                .id(collection.getId())
                .name(collection.getName())
                .description(collection.getDescription())
                .build();
    }
}
