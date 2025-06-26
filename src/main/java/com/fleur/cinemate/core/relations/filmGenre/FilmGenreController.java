package com.fleur.cinemate.core.relations.filmGenre;

import com.fleur.cinemate.core.relations.filmGenre.dto.CreateFilmGenreDto;
import com.fleur.cinemate.core.relations.filmGenre.dto.FilmGenreDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/films/{filmId}/genres")
public class FilmGenreController {

    private final FilmGenreService filmGenreService;

    @PostMapping
    public ResponseEntity<FilmGenreDto> addGenreToFilm(
            @PathVariable Long filmId,
            @RequestBody @Valid CreateFilmGenreDto createFilmGenreDto){
        return ResponseEntity.ok(filmGenreService.addGenreToFilm(filmId, createFilmGenreDto));
    }

    @GetMapping
    public ResponseEntity<List<FilmGenreDto>> getFilmGenres(
            @PathVariable Long filmId) {
        return ResponseEntity.ok(filmGenreService.findAllByFilm(filmId));
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<Void> deleteGenreFromFilm(
            @PathVariable Long filmId,
            @PathVariable Long genreId){
        filmGenreService.deleteGenreFromFilm(genreId, filmId);
        return ResponseEntity.noContent().build();
    }
}
