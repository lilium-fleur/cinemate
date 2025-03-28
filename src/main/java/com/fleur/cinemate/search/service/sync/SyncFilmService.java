package com.fleur.cinemate.search.service.sync;

import com.fleur.cinemate.core.actor.ActorService;
import com.fleur.cinemate.core.actor.dto.ActorDto;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmService;
import com.fleur.cinemate.core.genre.GenreService;
import com.fleur.cinemate.core.genre.dto.GenreDto;
import com.fleur.cinemate.search.document.FilmDocument;
import com.fleur.cinemate.search.entity.IndexName;
import com.fleur.cinemate.search.repository.ESSyncDateRepository;
import com.fleur.cinemate.search.repository.FilmDocumentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;


@Log4j2
@Service
public class SyncFilmService extends SyncService<Film> {

    private final FilmDocumentRepository filmDocumentRepository;
    private final GenreService genreService;
    private final ActorService actorService;
    private final FilmService filmService;

    public SyncFilmService(ESSyncDateRepository esSyncDateRepository,
                           FilmDocumentRepository filmDocumentRepository,
                           GenreService genreService,
                           ActorService actorService,
                           FilmService filmService) {
        super(esSyncDateRepository);
        this.filmDocumentRepository = filmDocumentRepository;
        this.genreService = genreService;
        this.actorService = actorService;
        this.filmService = filmService;
    }

    @Override
    protected void saveToIndex(Page<Film> filmPage) {
        for (Film film : filmPage) {
            Set<String> genres = genreService.findGenresByFilm(film.getId(), Pageable.unpaged())
                    .map(GenreDto::name)
                    .toSet();
            Set<String> actors = actorService.findActorsByFilm(film.getId(), Pageable.unpaged())
                    .map(ActorDto::name)
                    .toSet();
            try {
                filmDocumentRepository.save(convertToDocument(film, genres, actors));
            } catch (Exception e) {
                log.error("Error saving film document with id {} to index: {}",
                        film.getId(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected Page<Film> findEntitiesSinceDate(Instant sinceDate, Pageable pageable) {
        return filmService.findModifiedSince(sinceDate, pageable);
    }

    @Override
    protected Page<Film> findAllEntities(Pageable pageable) {
        return filmService.findAllFilmEntities(pageable);
    }

    @Override
    protected IndexName getIndexName() {
        return IndexName.FILMS;
    }

    private FilmDocument convertToDocument(Film film, Set<String> filmGenreNames, Set<String> filmActorNames) {
        return FilmDocument.builder()
                .id(film.getId())
                .title(film.getTitle())
                .description(film.getDescription())
                .releaseYear(film.getReleaseYear())
                .rating(film.getSourceRating())
                .suggest(new Completion(new String[]{film.getTitle()}))
                .actors(filmActorNames)
                .actorsSearch(String.join(" ", filmActorNames))
                .genres(filmGenreNames)
                .genresSearch(String.join(" ", filmGenreNames))
                .build();
    }

}
