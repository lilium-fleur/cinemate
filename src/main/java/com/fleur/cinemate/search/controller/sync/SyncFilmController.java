package com.fleur.cinemate.search.controller.sync;


import com.fleur.cinemate.search.service.sync.SyncFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/elasticsearch/films/sync")
public class SyncFilmController {
    private final SyncFilmService syncFilmService;

    @PostMapping("/full")
    public ResponseEntity<Void> addAllFilms() {
        syncFilmService.syncAll();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/incremental")
    public ResponseEntity<Void> addNewFilms() {
        syncFilmService.incrementalSync();
        return ResponseEntity.noContent().build();
    }
}
