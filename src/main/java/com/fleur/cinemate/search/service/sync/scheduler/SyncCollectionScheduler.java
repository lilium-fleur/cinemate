package com.fleur.cinemate.search.service.sync.scheduler;


import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.search.service.sync.SyncService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SyncCollectionScheduler extends SyncScheduler<Collection> {
    public SyncCollectionScheduler(SyncService<Collection> syncService) {
        super(syncService);
    }

    @Override
    protected void logSuccess() {
        log.info("Collection incremental sync completed");
    }

    @Override
    protected void logError(Exception e) {
        log.error("Collection incremental sync failed: {}", e.getMessage());
    }
}
