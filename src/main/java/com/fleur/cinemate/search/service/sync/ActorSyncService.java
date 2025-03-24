package com.fleur.cinemate.search.service.sync;

import com.fleur.cinemate.core.actor.Actor;
import com.fleur.cinemate.core.actor.ActorRepository;
import com.fleur.cinemate.search.document.ActorDocument;
import com.fleur.cinemate.search.entity.ESSyncDate;
import com.fleur.cinemate.search.entity.IndexName;
import com.fleur.cinemate.search.repository.ActorDocumentRepository;
import com.fleur.cinemate.search.repository.ESSyncDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class ActorSyncService {
    private final ActorRepository actorRepository;
    private final ActorDocumentRepository actorDocumentRepository;
    private final ESSyncDateRepository esSyncDateRepository;

    public void syncAllActors() {
        int page = 0;
        int size = 100;
        Page<Actor> actorPage;
        do{
            actorPage = actorRepository.findAll(PageRequest.of(page, size));
            for (Actor actor : actorPage) {
                actorDocumentRepository.save(convertToActorDocument(actor));
            }
            page++;
        }while(actorPage.hasNext());
    }

    public void incrementalSyncActors(){
        int page = 0;
        int size = 100;
        Page<Actor> actorPage;
        Instant lastSyncTime = esSyncDateRepository.findFirstByIndexNameOrderByLastSyncTime(IndexName.ACTORS)
                .map(ESSyncDate::getLastSyncTime)
                .orElse(Instant.EPOCH);
        do{
            actorPage = actorRepository.findModifiedSince(lastSyncTime, PageRequest.of(page, size));
            for (Actor actor : actorPage) {
                actorDocumentRepository.save(convertToActorDocument(actor));
            }
            page++;
        }while(actorPage.hasNext());
    }

    private ActorDocument convertToActorDocument(Actor actor) {
        return ActorDocument.builder()
                .id(actor.getId())
                .name(actor.getName())
                .build();
    }
}
