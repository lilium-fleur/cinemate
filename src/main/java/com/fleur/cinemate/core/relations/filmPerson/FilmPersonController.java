package com.fleur.cinemate.core.relations.filmPerson;

import com.fleur.cinemate.core.relations.filmPerson.dto.CreateFilmPersonDto;
import com.fleur.cinemate.core.relations.filmPerson.dto.FilmPersonDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/films/{filmId}/persons")
public class FilmPersonController {
    private final FilmPersonService filmPersonService;

    @GetMapping
    public ResponseEntity<Page<FilmPersonDto>> getFilmPersonsByFilm(
            @PathVariable Long filmId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(filmPersonService.findAllByFilm(filmId, pageable));
    }

    @PostMapping
    public ResponseEntity<FilmPersonDto> addPersonToFilm(
            @PathVariable Long filmId,
            @RequestBody @Valid CreateFilmPersonDto createFilmPersonDto) {
        return ResponseEntity.ok(filmPersonService.addPersonToFilm(filmId, createFilmPersonDto));
    }

    @DeleteMapping("/{filmPersonId}")
    public ResponseEntity<Void> deletePersonFromFilm(
            @PathVariable Long filmPersonId) {
        filmPersonService.deletePersonFromFilm(filmPersonId);
        return ResponseEntity.noContent().build();
    }

}
