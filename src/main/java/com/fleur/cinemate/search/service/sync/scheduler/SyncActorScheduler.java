package com.fleur.cinemate.search.service.sync.scheduler;

import com.fleur.cinemate.core.actor.Actor;
import com.fleur.cinemate.search.service.sync.SyncService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SyncActorScheduler extends SyncScheduler<Actor> {

    public SyncActorScheduler(SyncService<Actor> syncService) {
        super(syncService);
    }

    @Override
    protected void logSuccess() {
        log.info("Actor incremental sync completed");
    }

    @Override
    protected void logError(Exception e) {
        log.error("Actor incremental sync failed: {}", e.getMessage());
    }
}
