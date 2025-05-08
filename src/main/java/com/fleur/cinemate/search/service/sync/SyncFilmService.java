package com.fleur.cinemate.search.service.sync;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmService;
import com.fleur.cinemate.core.relations.filmGenre.FilmGenreService;
import com.fleur.cinemate.core.relations.filmPerson.FilmPersonService;
import com.fleur.cinemate.core.relations.filmPerson.model.FilmRole;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Log4j2
@Service
public class SyncFilmService extends SyncService<Film> {

    private final FilmDocumentRepository filmDocumentRepository;
    private final FilmService filmService;
    private final FilmPersonService filmPersonService;
    private final FilmGenreService filmGenreService;

    public SyncFilmService(ESSyncDateRepository esSyncDateRepository,
                           FilmDocumentRepository filmDocumentRepository,
                           FilmService filmService,
                           FilmPersonService filmPersonService,
                           FilmGenreService filmGenreService) {
        super(esSyncDateRepository);
        this.filmDocumentRepository = filmDocumentRepository;
        this.filmService = filmService;
        this.filmPersonService = filmPersonService;
        this.filmGenreService = filmGenreService;
    }

    @Override
    protected void saveToIndex(Page<Film> filmPage) {
        List<Long> filmIds = filmPage.map(Film::getId).toList();
        Map<Long, List<String>> genresByFilms = filmGenreService
                .findGenreNamesByFilms(filmIds);

        Map<Long, List<String>> actorsByFilms = filmPersonService
                .findPersonNamesByFilmAndRole(filmIds, FilmRole.ACTOR);

        Map<Long, List<String>> directorsByFilms = filmPersonService
                .findPersonNamesByFilmAndRole(filmIds, FilmRole.DIRECTOR);


        for (Film film : filmPage) {
            Set<String> genres = new HashSet<>(genresByFilms.getOrDefault(film.getId(), List.of()));
            Set<String> actors = new HashSet<>(actorsByFilms.getOrDefault(film.getId(), List.of()));
            Set<String> directors = new HashSet<>(directorsByFilms.getOrDefault(film.getId(), List.of()));
            try {
                filmDocumentRepository.save(convertToDocument(film, genres, actors, directors));
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

    private FilmDocument convertToDocument(Film film,
                                           Set<String> filmGenreNames,
                                           Set<String> filmActorNames,
                                           Set<String> filmDirectorNames) {
        return FilmDocument.builder()
                .id(film.getId())
                .title(film.getTitle())
                .description(film.getDescription())
                .releaseYear(film.getReleaseYear())
                .rating(film.getSourceRating())
                .suggest(new Completion(new String[]{film.getTitle()}))
                .genres(filmGenreNames)
                .genresSearch(String.join(" ", filmGenreNames))
                .actors(filmActorNames)
                .actorsSearch(String.join(" ", filmActorNames))
                .directors(filmDirectorNames)
                .directorsSearch(String.join(" ", filmDirectorNames))
                .build();
    }

}
