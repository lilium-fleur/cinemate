package com.fleur.cinemate.core.relations.filmGenre;

import com.fleur.cinemate.core.relations.filmGenre.dto.CreateFilmGenreDto;
import com.fleur.cinemate.core.relations.filmGenre.dto.FilmGenreDto;
import com.fleur.cinemate.core.relations.filmGenre.dto.FilmGenresDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/films")
public class FilmGenreController {

    private final FilmGenreService filmGenreService;

    @PostMapping("/{filmId}/genres")
    public ResponseEntity<FilmGenreDto> addGenreToFilm(
            @PathVariable Long filmId,
            @RequestBody @Valid CreateFilmGenreDto createFilmGenreDto){
        return ResponseEntity.ok(filmGenreService.addGenreToFilm(filmId, createFilmGenreDto));
    }

    @GetMapping("/{filmId}/genres")
    public ResponseEntity<FilmGenresDto> getFilmGenres(
            @PathVariable Long filmId) {
        return ResponseEntity.ok(filmGenreService.findGenreNamesByFilm(filmId));
    }

    @GetMapping("/genres")
    public ResponseEntity<List<FilmGenresDto>> getFilmGenresByFilmIds(
            @RequestParam(name = "ids") List<Long> filmIds) {
        return ResponseEntity.ok(filmGenreService.findGenreNamesByFilmsIds(filmIds));
    }

    @DeleteMapping("/{filmId}/genres/{genreId}")
    public ResponseEntity<Void> deleteGenreFromFilm(
            @PathVariable Long filmId,
            @PathVariable Long genreId){
        filmGenreService.deleteGenreFromFilm(genreId, filmId);
        return ResponseEntity.noContent().build();
    }
}
