package com.fleur.cinemate.pending;

import com.fleur.cinemate.search.service.ESDeleteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RetryDeleteTask {
    private final PendingDeleteRepository pendingDeleteRepository;
    private final ESDeleteService esDeleteService;

    @Async
    @Scheduled(fixedDelay = 340000)
    @Transactional(readOnly = true)
    public void retryPendingDeletes() {
        List<PendingDelete> pendingDeleteList = pendingDeleteRepository.findAll();
        for (PendingDelete pending : pendingDeleteList) {
            try {
                esDeleteService.deleteByEntityName(pending.getEntityName(), pending.getRecordId());
                pendingDeleteRepository.delete(pending);
            } catch (Exception e) {
                log.error("Retry failed for record {}, id: {}", pending.getEntityName(), pending.getRecordId());
            }
        }

    }
}
