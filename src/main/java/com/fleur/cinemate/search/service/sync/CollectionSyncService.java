package com.fleur.cinemate.search.service.sync;

import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.collection.CollectionRepository;
import com.fleur.cinemate.collection.collectionItem.CollectionItemService;
import com.fleur.cinemate.search.document.CollectionDocument;
import com.fleur.cinemate.search.entity.ESSyncDate;
import com.fleur.cinemate.search.entity.IndexName;
import com.fleur.cinemate.search.repository.CollectionDocumentRepository;
import com.fleur.cinemate.search.repository.ESSyncDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CollectionSyncService {
    private final CollectionRepository collectionRepository;
    private final CollectionDocumentRepository collectionDocumentRepository;
    private final ESSyncDateRepository esSyncDateRepository;
    private final CollectionItemService collectionItemService;


    public void syncAllCollections() {
        int page = 0;
        int size = 100;
        Page<Collection> collectionPage;
        do{
            collectionPage = collectionRepository.findAll(PageRequest.of(page, size));
            for(Collection collection : collectionPage){
                List<Long> itemIds = collectionItemService.findFilmIdsByCollection(collection.getId());
                collectionDocumentRepository.save(convertToCollectionDocument(collection, itemIds));
            }
            page++;
        }while(collectionPage.hasNext());
    }

    public void incrementalSyncCollections() {
        int page = 0;
        int size = 100;
        Page<Collection> collectionPage;
        Instant lastSyncTime = esSyncDateRepository.findFirstByIndexNameOrderByLastSyncTime(IndexName.COLLECTIONS)
                .map(ESSyncDate::getLastSyncTime)
                .orElse(Instant.EPOCH);
        do{
            collectionPage = collectionRepository.findModifiedSince(lastSyncTime, PageRequest.of(page, size));
            for(Collection collection : collectionPage){
                List<Long> itemIds = collectionItemService.findFilmIdsByCollection(collection.getId());
                collectionDocumentRepository.save(convertToCollectionDocument(collection, itemIds));
            }
            page++;
        }while(collectionPage.hasNext());
    }

    public CollectionDocument convertToCollectionDocument(Collection collection, List<Long> itemIds){
        return CollectionDocument.builder()
                .id(collection.getId())
                .userId(collection.getUser().getId())
                .name(collection.getName())
                .description(collection.getDescription())
                .filmIds(itemIds)
                .build();
    }
}
