package com.fleur.cinemate.search.service.sync;

import com.fleur.cinemate.core.actor.Actor;
import com.fleur.cinemate.core.actor.ActorService;
import com.fleur.cinemate.search.document.ActorDocument;
import com.fleur.cinemate.search.entity.IndexName;
import com.fleur.cinemate.search.repository.ActorDocumentRepository;
import com.fleur.cinemate.search.repository.ESSyncDateRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Log4j2
@Service
public class SyncActorService extends SyncService<Actor> {
    private final ActorDocumentRepository actorDocumentRepository;
    private final ActorService actorService;

    public SyncActorService(ESSyncDateRepository esSyncDateRepository,
                            ActorDocumentRepository actorDocumentRepository,
                            ActorService actorService) {
        super(esSyncDateRepository);
        this.actorDocumentRepository = actorDocumentRepository;
        this.actorService = actorService;
    }

    @Override
    protected void saveToIndex(Page<Actor> entityPage) {
        for (Actor actor : entityPage) {
            try {
                actorDocumentRepository.save(convertToDocument(actor));
            } catch (Exception e) {
                log.error("Error saving actor document with id {} to index: {}",
                        actor.getId(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected IndexName getIndexName() {
        return IndexName.ACTORS;
    }

    @Override
    protected Page<Actor> findAllEntities(Pageable pageable) {
        return actorService.findAllActorsEntities(pageable);
    }

    @Override
    protected Page<Actor> findEntitiesSinceDate(Instant sinceDate, Pageable pageable) {
        return actorService.findModifiedSince(sinceDate, pageable);
    }

    private ActorDocument convertToDocument(Actor actor) {
        return ActorDocument.builder()
                .id(actor.getId())
                .name(actor.getName())
                .build();
    }
}
