package com.fleur.cinemate.search.service.sync.scheduler;

import com.fleur.cinemate.core.person.Person;
import com.fleur.cinemate.search.service.sync.SyncService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SyncPersonScheduler extends SyncScheduler<Person> {

    public SyncPersonScheduler(SyncService<Person> syncService) {
        super(syncService);
    }

    @Override
    protected void logSuccess() {
        log.info("Person incremental sync completed");
    }

    @Override
    protected void logError(Exception e) {
        log.error("Person incremental sync failed: {}", e.getMessage());
    }
}
