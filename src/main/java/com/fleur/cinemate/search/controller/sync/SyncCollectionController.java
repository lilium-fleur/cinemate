package com.fleur.cinemate.search.controller.sync;

import com.fleur.cinemate.search.service.sync.SyncCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/elasticsearch/collections/sync")
public class SyncCollectionController {
    private final SyncCollectionService syncCollectionService;

    @PostMapping("/full")
    public ResponseEntity<Void> addAllFilms() {
        syncCollectionService.syncAll();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/incremental")
    public ResponseEntity<Void> addNewFilms() {
        syncCollectionService.incrementalSync();
        return ResponseEntity.noContent().build();
    }
}
