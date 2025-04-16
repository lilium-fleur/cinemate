package com.fleur.cinemate.core.genre;

import com.fleur.cinemate.core.genre.dto.CreateGenreDto;
import com.fleur.cinemate.core.genre.dto.GenreDto;
import com.fleur.cinemate.core.relations.filmGenre.FilmGenreService;
import com.fleur.cinemate.core.relations.filmGenre.dto.FilmGenreDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/genres")
@RestController
public class GenreController {
    private final GenreService genreService;
    private final FilmGenreService filmGenreService;

    @PostMapping
    public ResponseEntity<GenreDto> createGenre(
            @RequestBody @Valid CreateGenreDto createGenreDto) {
        return ResponseEntity.ok(genreService.createGenre(createGenreDto));
    }

    @GetMapping
    public ResponseEntity<Page<GenreDto>> getAllGenres(
            @PageableDefault(sort = "name") Pageable pageable) {
        return ResponseEntity.ok(genreService.findAllGenres(pageable));
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<GenreDto> getGenreById(
            @PathVariable Long genreId) {
        return ResponseEntity.ok(genreService.findGenreById(genreId));
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<Void> deleteGenre(
            @PathVariable Long genreId) {
        genreService.deleteGenre(genreId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{genreId}/films")
    public ResponseEntity<Page<FilmGenreDto>> getFilmsByGenre(
            @PathVariable Long genreId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(filmGenreService.findAllByGenre(genreId, pageable));
    }
}
