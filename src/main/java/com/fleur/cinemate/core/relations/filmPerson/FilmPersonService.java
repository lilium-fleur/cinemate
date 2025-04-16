package com.fleur.cinemate.core.relations.filmPerson;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmRepository;
import com.fleur.cinemate.core.person.Person;
import com.fleur.cinemate.core.person.PersonRepository;
import com.fleur.cinemate.core.relations.filmPerson.dto.CreateFilmPersonDto;
import com.fleur.cinemate.core.relations.filmPerson.dto.FilmPersonDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmPersonService {

    private final FilmPersonRepository filmPersonRepository;
    private final FilmRepository filmRepository;
    private final PersonRepository personRepository;
    private final FilmPersonMapper filmPersonMapper;

    @Transactional
    public FilmPersonDto addPersonToFilm(Long filmId, CreateFilmPersonDto createFilmPersonDto) {
        try {
            Role role = Role.valueOf(createFilmPersonDto.role());
            filmPersonRepository.findByFilmIdAndPersonIdAndRole(filmId, createFilmPersonDto.personId(), role)
                    .ifPresent(filmPerson -> {
                        throw new BadRequestException(String.format
                                ("Person %s with role %s already added to film %s",
                                        createFilmPersonDto.personId(), role.name(), filmId));
                    });

            Film film = filmRepository.findById(filmId)
                    .orElseThrow(() -> new EntityNotFoundException("Film not found"));
            Person person = personRepository.findById(createFilmPersonDto.personId())
                    .orElseThrow(() -> new EntityNotFoundException("Person not found"));

            FilmPerson filmPerson = FilmPerson.builder()
                    .film(film)
                    .person(person)
                    .build();

            return filmPersonMapper.toDto(filmPersonRepository.save(filmPerson));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(String.format("Invalid role type: %s", e.getMessage()));
        }
    }

    @Transactional
    public void deletePersonFromFilm(Long filmPersonId) {
        FilmPerson filmPerson = filmPersonRepository.findById(filmPersonId)
                .orElseThrow(() -> new EntityNotFoundException("Record FilmPerson not found"));

        filmPersonRepository.delete(filmPerson);
    }

    @Transactional(readOnly = true)
    public Page<FilmPersonDto> findAllByFilm(Long filmId, Pageable pageable) {
        return filmPersonRepository.findAllByFilmId(filmId, pageable)
                .map(filmPersonMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<String> findPersonNamesByFilmAndRole(Long filmId, Role role) {
        return filmPersonRepository.findAllByFilmIdAndRole(filmId, role).stream()
                .map(FilmPerson::getPerson)
                .map(Person::getName)
                .toList();

    }

    @Transactional(readOnly = true)
    public Page<FilmPersonDto> findAllByPerson(Long personId, Pageable pageable) {
        return filmPersonRepository.findAllByPersonId(personId, pageable)
                .map(filmPersonMapper::toDto);
    }

}
