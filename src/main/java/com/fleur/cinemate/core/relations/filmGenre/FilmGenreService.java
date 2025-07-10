package com.fleur.cinemate.core.relations.filmGenre;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmRepository;
import com.fleur.cinemate.core.genre.Genre;
import com.fleur.cinemate.core.genre.GenreRepository;
import com.fleur.cinemate.core.relations.filmGenre.dto.CreateFilmGenreDto;
import com.fleur.cinemate.core.relations.filmGenre.dto.FilmGenreDto;
import com.fleur.cinemate.core.relations.filmGenre.dto.FilmGenresDto;
import com.fleur.cinemate.core.relations.filmGenre.model.FilmGenre;
import com.fleur.cinemate.core.relations.filmGenre.model.FilmGenreProjection;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmGenreService {

    private final FilmGenreRepository filmGenreRepository;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final FilmGenreMapper filmGenreMapper;


    @Transactional
    public FilmGenreDto addGenreToFilm(Long filmId, CreateFilmGenreDto createFilmGenreDto) {
        filmGenreRepository.findByFilmIdAndGenreId(filmId, createFilmGenreDto.genreId())
                .ifPresent(it -> {
                    throw new BadRequestException("Genre already added to this Film");
                });
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));
        Genre genre = genreRepository.findById(createFilmGenreDto.genreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));

        FilmGenre filmGenre = FilmGenre.builder()
                .film(film)
                .genre(genre)
                .build();
        return filmGenreMapper.toDto(filmGenreRepository.save(filmGenre));
    }

    @Transactional
    public void deleteGenreFromFilm(Long filmId, Long genreId) {
        FilmGenre filmGenre = filmGenreRepository.findByFilmIdAndGenreId(filmId, genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre was not added to this Film"));

        filmGenreRepository.delete(filmGenre);
    }

    @Transactional(readOnly = true)
    public FilmGenresDto findGenreNamesByFilm(Long filmId) {
        List<String> genreNames = filmGenreRepository.findByFilmId(filmId);
        return FilmGenresDto.builder()
                .genreNames(genreNames)
                .filmId(filmId)
                .build();
    }

    @Transactional(readOnly = true)
    public List<FilmGenresDto> findGenreNamesByFilmsIds(List<Long> filmIds) {
        return filmGenreRepository.findGenresByFilmIds(filmIds).stream()
                .collect(Collectors.groupingBy(FilmGenreProjection::getFilmId,
                        Collectors.mapping(FilmGenreProjection::getGenreName, Collectors.toList())))
                .entrySet().stream()
                .map(e -> FilmGenresDto.builder()
                        .filmId(e.getKey())
                        .genreNames(e.getValue())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<Long, List<String>> findGenreNamesByFilms(List<Long> filmIds) {
        return filmGenreRepository.findByFilmIds(filmIds).stream()
                .collect(Collectors.groupingBy(FilmGenreProjection::getFilmId,
                        Collectors.mapping(FilmGenreProjection::getGenreName, Collectors.toList())));
    }

}
