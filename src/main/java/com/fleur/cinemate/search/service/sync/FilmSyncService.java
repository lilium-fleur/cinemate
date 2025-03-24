package com.fleur.cinemate.search.service.sync;

import com.fleur.cinemate.core.actor.ActorService;
import com.fleur.cinemate.core.actor.dto.ActorDto;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmRepository;
import com.fleur.cinemate.core.genre.GenreService;
import com.fleur.cinemate.core.genre.dto.GenreDto;
import com.fleur.cinemate.search.document.FilmDocument;
import com.fleur.cinemate.search.entity.ESSyncDate;
import com.fleur.cinemate.search.entity.IndexName;
import com.fleur.cinemate.search.repository.ESSyncDateRepository;
import com.fleur.cinemate.search.repository.FilmDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class FilmSyncService {

    private final FilmRepository filmRepository;
    private final FilmDocumentRepository filmDocumentRepository;
    private final GenreService genreService;
    private final ActorService actorService;
    private final ESSyncDateRepository esSyncDateRepository;

    public void syncAllFilms() {
        int page = 0;
        int size = 100;
        Page<Film> filmPage;
        do {
            filmPage = filmRepository.findAll(PageRequest.of(page, size));
            saveFilms(filmPage);
            page++;
        } while (filmPage.hasNext());
    }

    public void incrementalSyncFilms(){
        int page = 0;
        int size = 100;
        Instant lastSyncTime = esSyncDateRepository.findFirstByIndexNameOrderByLastSyncTime(IndexName.FILMS)
                .map(ESSyncDate::getLastSyncTime)
                .orElse(Instant.EPOCH);
        Page<Film> filmPage;
        do {
            filmPage = filmRepository.findModifiedSince(lastSyncTime, PageRequest.of(page, size));
            saveFilms(filmPage);
            page++;
        } while (filmPage.hasNext());
    }


    private void saveFilms(Page<Film> filmPage) {
        for (Film film : filmPage) {
            Set<String> genres = genreService.findGenresByFilm(film.getId(), Pageable.unpaged())
                    .map(GenreDto::name)
                    .toSet();
            Set<String> actors = actorService.findActorsByFilm(film.getId(), Pageable.unpaged())
                    .map(ActorDto::name)
                    .toSet();

            filmDocumentRepository.save(convertToFilmDocument(film, genres, actors));
        }
    }

    private FilmDocument convertToFilmDocument(Film film, Set<String> filmGenreNames, Set<String> filmActorNames) {
        return FilmDocument.builder()
                .id(film.getId())
                .title(film.getTitle())
                .description(film.getDescription())
                .releaseYear(film.getReleaseYear())
                .actors(filmActorNames)
                .actorsSearch(filmActorNames)
                .genres(filmGenreNames)
                .genresSearch(filmGenreNames)
                .build();
    }

}
