package com.fleur.cinemate.search.service.sync.scheduler;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.search.service.sync.SyncService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SyncFilmScheduler extends SyncScheduler<Film> {
    public SyncFilmScheduler(SyncService<Film> syncService) {
        super(syncService);
    }

    @Override
    protected void logSuccess() {
        log.info("Film incremental sync completed");
    }

    @Override
    protected void logError(Exception e) {
        log.error("Film incremental sync failed: {}", e.getMessage());
    }
}
