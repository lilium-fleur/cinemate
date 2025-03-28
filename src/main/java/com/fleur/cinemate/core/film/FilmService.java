package com.fleur.cinemate.core.film;


import com.fleur.cinemate.core.film.dto.CreateFilmDto;
import com.fleur.cinemate.core.film.dto.FilmDto;
import com.fleur.cinemate.core.film.dto.UpdateFilmDto;
import com.fleur.cinemate.core.relations.filmActor.FilmActor;
import com.fleur.cinemate.core.relations.filmActor.FilmActorRepository;
import com.fleur.cinemate.core.relations.filmGenre.FilmGenre;
import com.fleur.cinemate.core.relations.filmGenre.FilmGenreRepository;
import com.fleur.cinemate.search.repository.FilmDocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final FilmActorRepository filmActorRepository;
    private final FilmGenreRepository filmGenreRepository;
    private final FilmDocumentRepository filmDocumentRepository;


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

        try {
            filmDocumentRepository.deleteById(film.getId());
        } catch (Exception e) {
            log.warn("Error deleting from elastic-index film with id:{}", filmId);
        }
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
    public Page<FilmDto> findAllFilms(Pageable pageable){
        return filmRepository.findAll(pageable).map(filmMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<Film> findAllFilmEntities(Pageable pageable){
        return filmRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Film> findModifiedSince(Instant since, Pageable pageable){
        return filmRepository.findModifiedSince(since, pageable);
    }

    @Transactional(readOnly = true)
    public List<FilmDto> findAllFilmsById(List<Long> ids){
        return filmRepository.findAllById(ids).stream()
                .map(filmMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<FilmDto> findFilmsByActor(Long actorId, Pageable pageable){
        return filmActorRepository.findByActorId(actorId, pageable)
                .map(FilmActor::getFilm)
                .map(filmMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<FilmDto> findFilmsByGenre(Long genreId, Pageable pageable) {
        return filmGenreRepository.findByGenreId(genreId, pageable)
                .map(FilmGenre::getFilm)
                .map(filmMapper::toDto);
    }

}
