package com.fleur.cinemate.core.film;

import com.fleur.cinemate.core.film.dto.CreateFilmDto;
import com.fleur.cinemate.core.film.dto.FilmDto;
import com.fleur.cinemate.core.film.dto.UpdateFilmDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(
            @RequestBody @Valid CreateFilmDto createdFilmDto) {
        return ResponseEntity.ok(filmService.createFilm(createdFilmDto));
    }

    @GetMapping
    public ResponseEntity<Page<FilmDto>> getFilms(
            @PageableDefault(sort = "title") Pageable pageable) {
        return ResponseEntity.ok(filmService.findAllFilms(pageable));
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<FilmDto> getFilmById(
            @PathVariable Long filmId) {
        return ResponseEntity.ok(filmService.findFilmById(filmId));
    }

    @PutMapping("/{filmId}")
    public ResponseEntity<FilmDto> updateFilm(
            @PathVariable Long filmId,
            @RequestBody UpdateFilmDto updatedFilmDto) {
        return ResponseEntity.ok(filmService.updateFilm(updatedFilmDto, filmId));
    }

    @DeleteMapping("/{filmId}")
    public ResponseEntity<Void> deleteFilm(
            @PathVariable Long filmId) {
        filmService.deleteFilm(filmId);
        return ResponseEntity.noContent().build();
    }



}
