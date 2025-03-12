package com.fleur.cinemate.userCollection.collection.filmCollection;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userCollection.collection.filmCollection.dto.CreateFilmCollectionDto;
import com.fleur.cinemate.userCollection.collection.filmCollection.dto.FilmCollectionDto;
import com.fleur.cinemate.userCollection.collection.filmCollection.dto.UpdateFilmCollectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/film-collections")
public class FilmCollectionController {

    private final FilmCollectionService filmCollectionService;


    @PostMapping
    public ResponseEntity<FilmCollectionDto> createFilmCollection(
            @RequestBody CreateFilmCollectionDto createFilmCollectionDto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(filmCollectionService.createFilmCollection(createFilmCollectionDto, user));
    }


    @GetMapping("/{filmCollectionId}")
    public ResponseEntity<FilmCollectionDto> getFilmCollectionById(
            @PathVariable Long filmCollectionId,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(filmCollectionService.findFilmCollectionById(filmCollectionId, user));
    }

    @PutMapping("/{filmCollectionId}")
    public ResponseEntity<FilmCollectionDto> updateFilmCollection(
            @PathVariable Long filmCollectionId,
            @RequestBody UpdateFilmCollectionDto updateFilmCollectionDto,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(
                filmCollectionService.updateFilmCollection(
                        updateFilmCollectionDto, filmCollectionId, user));
    }

    @DeleteMapping("/{filmCollectionId}")
    public ResponseEntity<FilmCollectionDto> deleteFilmCollection(
            @PathVariable Long filmCollectionId,
            @AuthenticationPrincipal User user){
        filmCollectionService.deleteFilmCollection(filmCollectionId, user);
        return ResponseEntity.noContent().build();
    }



}
