package com.fleur.cinemate.listener;

import com.fleur.cinemate.event.RecordDeletedEvent;
import com.fleur.cinemate.pending.PendingDelete;
import com.fleur.cinemate.pending.PendingDeleteRepository;
import com.fleur.cinemate.search.service.ESDeleteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Log4j2
@Component
public class RecordDeleteEventListener {
    private final ESDeleteService esDeleteService;
    private final PendingDeleteRepository pendingDeleteRepository;

    @Async
    @EventListener
    @Transactional
    public void handleRecordDeleted(RecordDeletedEvent event) {
        if (event == null || event.getRecordId() == null
                || event.getEntityName() == null
                || event.getEntityName().isBlank()) {
            log.warn("RecordDeleted event is null or empty");
            return;
        }
        Long recordId = event.getRecordId();
        String entityName = event.getEntityName();
        try {
            esDeleteService.deleteByEntityName(entityName, recordId);
            log.info("Record {} with id: {} has been deleted", entityName, recordId);
        } catch (Exception e) {
            log.error("Deleting record {} with id: {} failed: {}", entityName, recordId, e.getMessage());
            pendingDeleteRepository.save(new PendingDelete(recordId, entityName));
        }
    }
}
