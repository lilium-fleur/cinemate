package com.fleur.cinemate.core.relations.filmGenre;

import com.fleur.cinemate.core.relations.filmGenre.dto.CreateFilmGenreDto;
import com.fleur.cinemate.core.relations.filmGenre.dto.FilmGenreDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<FilmGenreDto>> getFilmGenres(
            @PathVariable Long filmId,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(filmGenreService.findAllByFilm(filmId, pageable));
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<Void> deleteGenreFromFilm(
            @PathVariable Long filmId,
            @PathVariable Long genreId){
        filmGenreService.deleteGenreFromFilm(genreId, filmId);
        return ResponseEntity.noContent().build();
    }
}
