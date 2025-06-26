package com.fleur.cinemate.search.controller.query;


import com.fleur.cinemate.core.film.dto.FilmDto;
import com.fleur.cinemate.search.dto.FilmFilter;
import com.fleur.cinemate.search.service.query.SearchFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/films/search")
public class SearchFilmController {
    private final SearchFilmService searchFilmService;

    @PostMapping
    public ResponseEntity<Page<FilmDto>> searchFilms(
            @RequestBody FilmFilter filter,
            @PageableDefault Pageable pageable)     {
        return ResponseEntity.ok(searchFilmService.findFilmsByFilters(filter, pageable));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> suggest(
            @RequestParam(name = "q") String q){
        return ResponseEntity.ok(searchFilmService.findSuggest(q));
    }
}
