package com.fleur.cinemate.core.ralations.filmActor;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.core.actor.Actor;
import com.fleur.cinemate.core.actor.ActorRepository;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmRepository;
import com.fleur.cinemate.core.ralations.filmActor.dto.CreateFilmActorDto;
import com.fleur.cinemate.core.ralations.filmActor.dto.FilmActorDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FilmActorService {

    private final FilmActorRepository filmActorRepository;
    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;
    private final FilmActorMapper filmActorMapper;

    @Transactional
    public FilmActorDto addActorToFilm(Long filmId, CreateFilmActorDto createFilmActorDto) {
        filmActorRepository.findByFilmIdAndActorId(filmId, createFilmActorDto.actorId())
                .ifPresent(filmActor -> {
                    throw new BadRequestException("Actor already added to this Film");
                });
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));
        Actor actor = actorRepository.findById(createFilmActorDto.actorId())
                .orElseThrow(() -> new EntityNotFoundException("Actor not found"));

        FilmActor filmActor = FilmActor.builder()
                .film(film)
                .actor(actor)
                .build();

        return filmActorMapper.toDto(filmActorRepository.save(filmActor));
    }

    @Transactional
    public void deleteActorFromFilm(Long filmId, Long actorId) {
        FilmActor filmActor = filmActorRepository.findByFilmIdAndActorId(filmId, actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor was not added to this Film"));

        filmActorRepository.delete(filmActor);
    }



}
