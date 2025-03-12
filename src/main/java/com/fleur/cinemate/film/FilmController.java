package com.fleur.cinemate.film;

import com.fleur.cinemate.actor.dto.ActorDto;
import com.fleur.cinemate.film.dto.CreateFilmDto;
import com.fleur.cinemate.film.dto.FilmDto;
import com.fleur.cinemate.film.dto.UpdateFilmDto;
import com.fleur.cinemate.genre.dto.GenreDto;
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
            @PageableDefault(sort = "title") Pageable pageable){
        return ResponseEntity.ok(filmService.findAllFilms(pageable));
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<FilmDto> getFilmById(
            @PathVariable Long filmId){
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
            @PathVariable Long filmId){
        filmService.deleteFilm(filmId);
        return ResponseEntity.noContent().build();
    }

//    жанры и актеры конкретного фильма

    @GetMapping("/{filmId}/genres")
    public ResponseEntity<Page<GenreDto>> getGenresByFilm(
            @PathVariable Long filmId,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(filmService.getGenresByFilm(filmId, pageable));
    }

    @PostMapping("/{filmId}/genres")
    public ResponseEntity<> addGenreToFilm(

    )

    @GetMapping("/{filmId}/actors")
    public ResponseEntity<Page<ActorDto>> getActorsByFilm(
            @PathVariable Long filmId,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(filmService.getActorsByFilm(filmId, pageable));
    }


//    нужен какой то отдельный контроллер именно для поиска по фильтрам
//    @GetMapping("/search/all")
//    public ResponseEntity<Page<FilmDto>> getFilms(
//            @PageableDefault(sort = "title") Pageable pageable){
//        return ResponseEntity.ok(filmService.findAllFilms(pageable));
//    }
//
//    @GetMapping("search/genre/{genreId}")
//    public ResponseEntity<Page<FilmDto>> getFilmsByGenre(
//            @PathVariable Long genreId,
//            @PageableDefault(sort = "title") Pageable pageable){
//        return ResponseEntity.ok(filmService.findFilmsByGenre(genreId, pageable));
//    }
//
//    @GetMapping("/search/actor/{actorId}")
//    public ResponseEntity<Page<FilmDto>> getFilmsByActor(
//            @PathVariable Long actorId,
//            @PageableDefault(sort = "title") Pageable pageable){
//        return ResponseEntity.ok(filmService.findFilmsByActorId(actorId, pageable));
//    }

}
