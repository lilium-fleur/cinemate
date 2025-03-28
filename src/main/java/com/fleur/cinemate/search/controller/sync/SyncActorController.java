package com.fleur.cinemate.search.controller.sync;

import com.fleur.cinemate.search.service.sync.SyncActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/elasticsearch/actors/sync")
public class SyncActorController {
    private final SyncActorService syncActorService;

    @PostMapping("/full")
    public ResponseEntity<Void> addAllFilms() {
        syncActorService.syncAll();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/incremental")
    public ResponseEntity<Void> addNewFilms() {
        syncActorService.incrementalSync();
        return ResponseEntity.noContent().build();
    }
}
