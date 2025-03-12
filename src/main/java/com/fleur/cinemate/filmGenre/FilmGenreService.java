package com.fleur.cinemate.filmGenre;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.film.Film;
import com.fleur.cinemate.genre.Genre;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FilmGenreService {
    private final FilmGenreRepository filmGenreRepository;


    @Transactional
    public FilmGenre addGenreToFilm(Film film, Genre genre){
        filmGenreRepository.findByFilmIdAndGenreId(film.getId(), genre.getId())
                .ifPresent(it -> {
                    throw new BadRequestException("Genre already added to this Film");
                });
        FilmGenre filmGenre = FilmGenre.builder()
                .film(film)
                .genre(genre)
                .build();
        return filmGenreRepository.save(filmGenre);
    }

    @Transactional
    public void deleteGenreFromFilm(Film film, Genre genre){
        FilmGenre filmGenre = filmGenreRepository.findByFilmIdAndGenreId(film.getId(), genre.getId())
                .orElseThrow(() -> new EntityNotFoundException("Genre was not added to this Film"));

        filmGenreRepository.delete(filmGenre);
    }

    @Transactional(readOnly = true)
    public Page<FilmGenre> getAllByGenre(Genre genre, Pageable pageable){
        return filmGenreRepository.findByGenre(genre, pageable);
    }

    @Transactional(readOnly = true)
    public Page<FilmGenre> getAllByFilm(Film film, Pageable pageable){
        return filmGenreRepository.findByFilm(film, pageable);
    }
}
