package com.fleur.cinemate.filmActor;


import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.actor.Actor;
import com.fleur.cinemate.film.Film;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FilmActorService {

    private final FilmActorRepository filmActorRepository;

    @Transactional
    public FilmActor addActorToFilm(Film film, Actor actor) {
        filmActorRepository.findByFilmAndActor(film, actor)
                .ifPresent(filmActor -> {
                    throw new BadRequestException("Actor already added to this Film");
                });

        FilmActor filmActor = FilmActor.builder()
                .film(film)
                .actor(actor)
                .build();

        return filmActorRepository.save(filmActor);
    }

    @Transactional
    public void deleteActorFromFilm(Film film, Actor actor) {
        FilmActor filmActor = filmActorRepository.findByFilmAndActor(film, actor)
                .orElseThrow(() -> new EntityNotFoundException("Actor was not added to this Film"));

        filmActorRepository.delete(filmActor);
    }

    @Transactional(readOnly = true)
    public Page<FilmActor> getAllByActor(Actor actor, Pageable pageable) {
        return filmActorRepository.findByActor(actor, pageable);
    }

    @Transactional(readOnly = true)
    public Page<FilmActor> getAllByFilm(Film film, Pageable pageable) {
        return filmActorRepository.findByFilm(film, pageable);
    }
}
