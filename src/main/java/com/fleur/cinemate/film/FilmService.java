package com.fleur.cinemate.film;

import com.fleur.cinemate.actor.Actor;
import com.fleur.cinemate.actor.ActorMapper;
import com.fleur.cinemate.actor.ActorService;
import com.fleur.cinemate.actor.dto.ActorDto;
import com.fleur.cinemate.film.dto.CreateFilmDto;
import com.fleur.cinemate.film.dto.FilmDto;
import com.fleur.cinemate.film.dto.UpdateFilmDto;
import com.fleur.cinemate.filmActor.FilmActor;
import com.fleur.cinemate.filmActor.FilmActorService;
import com.fleur.cinemate.filmGenre.FilmGenre;
import com.fleur.cinemate.filmGenre.FilmGenreRepository;
import com.fleur.cinemate.filmGenre.FilmGenreService;
import com.fleur.cinemate.genre.Genre;
import com.fleur.cinemate.genre.GenreMapper;
import com.fleur.cinemate.genre.GenreService;
import com.fleur.cinemate.genre.dto.GenreDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final GenreService genreService;
    private final FilmGenreService filmGenreService;
    private final FilmGenreRepository filmGenreRepository;
    private final GenreMapper genreMapper;
    private final ActorService actorService;
    private final FilmActorService filmActorService;
    private final ActorMapper actorMapper;

    @Transactional
    public FilmDto createFilm(CreateFilmDto createFilmDto){
        Film newFilm = filmMapper.toEntity(createFilmDto);

        return filmMapper.toDto(filmRepository.save(newFilm));
    }

    @Transactional
    public FilmDto updateFilm(UpdateFilmDto updateFilmDto, Long filmId){
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));

        filmMapper.updateEntityFromDto(updateFilmDto, film);

        return filmMapper.toDto(filmRepository.save(film));
    }

    @Transactional
    public void deleteFilm(Long filmId){
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));

        filmRepository.delete(film);
    }

    @Transactional(readOnly = true)
    public FilmDto findFilmById(Long filmId){
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));

        return filmMapper.toDto(film);
    }
    @Transactional(readOnly = true)
    public Film findFilmEntityById(Long filmId){
        return filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));
    }

    @Transactional(readOnly = true)
    public Page<FilmDto> findFilmsByGenre(Long genreId, Pageable pageable){
        Genre genre = genreService.findGenreEntityById(genreId);

        return filmGenreService.getAllByGenre(genre, pageable)
                .map(FilmGenre::getFilm)
                .map(filmMapper::toDto); 

    }

    @Transactional(readOnly = true)
    public Page<GenreDto> getGenresByFilm(Long filmId, Pageable pageable){
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));
        return filmGenreRepository.findByFilm(film, pageable)
                .map(FilmGenre::getGenre)
                .map(genreMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<FilmDto> findFilmsByActorId(Long actorId, Pageable pageable){
        Actor actor = actorService.findActorEntityById(actorId);

        return filmActorService.getAllByActor(actor, pageable)
                .map(FilmActor::getFilm)
                .map(filmMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ActorDto> getActorsByFilm(Long filmId, Pageable pageable){
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));

        return filmActorService.getAllByFilm(film, pageable)
                .map(FilmActor::getActor)
                .map(actorMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<FilmDto> findAllFilms(Pageable pageable){
        return filmRepository.findAll(pageable).map(filmMapper::toDto);
    }



}
