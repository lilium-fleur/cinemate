package com.fleur.cinemate.genre;

import com.fleur.cinemate.genre.dto.CreateGenreDto;
import com.fleur.cinemate.genre.dto.GenreDto;
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

    @PostMapping
    public ResponseEntity<GenreDto> createGenre(
            @RequestBody @Valid CreateGenreDto createGenreDto){
        return ResponseEntity.ok(genreService.createGenre(createGenreDto));
    }

    @GetMapping
    public ResponseEntity<Page<GenreDto>> getAllGenres(
            @PageableDefault(sort = "name") Pageable pageable){
        return ResponseEntity.ok(genreService.findAllGenres(pageable));
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<GenreDto> getGenreById(
            @PathVariable Long genreId){
        return ResponseEntity.ok(genreService.findGenreById(genreId));
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<Void> deleteGenre(
            @PathVariable Long genreId){
        genreService.deleteGenre(genreId);
        return ResponseEntity.noContent().build();
    }
}
