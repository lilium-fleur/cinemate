package com.fleur.cinemate.core.person;

import com.fleur.cinemate.core.person.dto.CreatePersonDto;
import com.fleur.cinemate.core.person.dto.PersonDto;
import com.fleur.cinemate.core.person.dto.UpdatePersonDto;
import com.fleur.cinemate.core.relations.filmPerson.FilmPersonService;
import com.fleur.cinemate.core.relations.filmPerson.dto.FilmPersonDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final FilmPersonService filmPersonService;

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(
            @RequestBody @Valid CreatePersonDto createPersonDto) {
        return ResponseEntity.ok(personService.createPerson(createPersonDto));
    }

    @GetMapping
    public ResponseEntity<Page<PersonDto>> getPersons(
            @PageableDefault(sort = "name") Pageable pageable){
        return ResponseEntity.ok(personService.findAllPersons(pageable));
    }

    @GetMapping("/{personId}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(personService.findPersonById(personId));
    }

    @PutMapping("/{personId}")
    public ResponseEntity<PersonDto> updatePerson(
            @PathVariable Long personId,
            @RequestBody UpdatePersonDto updatePersonDto) {
        return ResponseEntity.ok(personService.updatePerson(updatePersonDto, personId));
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long personId) {
        personService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{personId}/films")
    public ResponseEntity<Page<FilmPersonDto>> getPersonFilms(
            @PathVariable Long personId,
            @PageableDefault(sort = "name") Pageable pageable) {
        return ResponseEntity.ok(filmPersonService.findAllByPerson(personId, pageable));
    }

}
