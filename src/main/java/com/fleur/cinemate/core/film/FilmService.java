package com.fleur.cinemate.core.film;


import com.fleur.cinemate.core.film.dto.CreateFilmDto;
import com.fleur.cinemate.core.film.dto.FilmDto;
import com.fleur.cinemate.core.film.dto.UpdateFilmDto;
import com.fleur.cinemate.event.RecordDeletedEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final ApplicationEventPublisher eventPublisher;


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

        eventPublisher.publishEvent(new RecordDeletedEvent(this, filmId, "Film"));
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

    @Transactional
    public List<FilmDto> createSomeFilms(List<CreateFilmDto> createFilmDtos) {
        List<FilmDto> newFilms = new ArrayList<>();
        for (CreateFilmDto createFilmDto : createFilmDtos) {
            Film newFilm = filmMapper.toEntity(createFilmDto);
            FilmDto dto = filmMapper.toDto(filmRepository.save(newFilm));
            newFilms.add(dto);
        }
        return newFilms;
    }
}
