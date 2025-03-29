package com.fleur.cinemate.search.service.sync.scheduler;

import com.fleur.cinemate.search.service.sync.SyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public abstract class SyncScheduler<T> {

    private final SyncService<T> syncService;

    public SyncScheduler(SyncService<T> syncService) {
        this.syncService = syncService;
    }

    @Async
    @Scheduled(fixedRateString = "${sync.scheduler.incremental.interval}")
    public void runIncrementalSync(){
        try {
            syncService.incrementalSync();
            logSuccess();
        } catch (Exception e) {
            logError(e);
        }
    }

    abstract protected void logSuccess();
    abstract protected void logError(Exception e);
}
