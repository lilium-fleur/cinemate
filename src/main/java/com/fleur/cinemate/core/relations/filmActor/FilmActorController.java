package com.fleur.cinemate.core.relations.filmActor;

import com.fleur.cinemate.core.actor.ActorService;
import com.fleur.cinemate.core.actor.dto.ActorDto;
import com.fleur.cinemate.core.relations.filmActor.dto.CreateFilmActorDto;
import com.fleur.cinemate.core.relations.filmActor.dto.FilmActorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/films/{filmId}/actors")
public class FilmActorController {
    private final FilmActorService filmActorService;
    private final ActorService actorService;


    @GetMapping
    public ResponseEntity<Page<ActorDto>> getFilmActors(
            @PathVariable Long filmId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(actorService.findActorsByFilm(filmId, pageable));
    }
    @PostMapping
    public ResponseEntity<FilmActorDto> addActorToFilm(
            @PathVariable Long filmId,
            @RequestBody @Valid CreateFilmActorDto createFilmActorDto){
        return ResponseEntity.ok(filmActorService.addActorToFilm(filmId, createFilmActorDto));
    }

    @DeleteMapping("/{actorId}")
    public ResponseEntity<Void> deleteActorFromFilm(
            @PathVariable Long filmId,
            @PathVariable Long actorId){
        filmActorService.deleteActorFromFilm(actorId, filmId);
        return ResponseEntity.noContent().build();
    }

}
