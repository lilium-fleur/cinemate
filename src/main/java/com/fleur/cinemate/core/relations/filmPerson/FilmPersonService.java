package com.fleur.cinemate.core.relations.filmPerson;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmRepository;
import com.fleur.cinemate.core.person.Person;
import com.fleur.cinemate.core.person.PersonRepository;
import com.fleur.cinemate.core.relations.filmPerson.dto.CreateFilmPersonDto;
import com.fleur.cinemate.core.relations.filmPerson.dto.FilmPersonDto;
import com.fleur.cinemate.core.relations.filmPerson.model.FilmPerson;
import com.fleur.cinemate.core.relations.filmPerson.model.FilmPersonProjection;
import com.fleur.cinemate.core.relations.filmPerson.model.FilmRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmPersonService {

    private final FilmPersonRepository filmPersonRepository;
    private final FilmRepository filmRepository;
    private final PersonRepository personRepository;
    private final FilmPersonMapper filmPersonMapper;

    @Transactional
    public FilmPersonDto addPersonToFilm(Long filmId, CreateFilmPersonDto createFilmPersonDto) {
        FilmRole filmRole = FilmRole.fromString(createFilmPersonDto.role());
        filmPersonRepository.findByFilmIdAndPersonIdAndFilmRole(filmId, createFilmPersonDto.personId(), filmRole)
                .ifPresent(filmPerson -> {
                    throw new BadRequestException(String.format
                            ("Person %s with role %s already added to film %s",
                                    createFilmPersonDto.personId(), filmRole.name(), filmId));
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
    public Map<Long, List<String>> findPersonNamesByFilmAndRole(List<Long> filmIds, FilmRole filmRole) {
        return filmPersonRepository.findPersonNamesByFilmIdAndRole(filmIds, filmRole).stream()
                .collect(Collectors.groupingBy(FilmPersonProjection::getFilmId,
                        Collectors.mapping(FilmPersonProjection::getPersonName, Collectors.toList())));

    }

    @Transactional(readOnly = true)
    public Page<FilmPersonDto> findAllByPerson(Long personId, Pageable pageable) {
        return filmPersonRepository.findAllByPersonId(personId, pageable)
                .map(filmPersonMapper::toDto);
    }

}
