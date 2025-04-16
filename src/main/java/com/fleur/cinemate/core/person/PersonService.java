package com.fleur.cinemate.core.person;

import com.fleur.cinemate.core.person.dto.CreatePersonDto;
import com.fleur.cinemate.core.person.dto.PersonDto;
import com.fleur.cinemate.core.person.dto.UpdatePersonDto;
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
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PersonDto createPerson(CreatePersonDto createPersonDto) {
        Person person = personMapper.toEntity(createPersonDto);
        return personMapper.toDto(personRepository.save(person));
    }

    @Transactional
    public PersonDto updatePerson(UpdatePersonDto updatePersonDto, Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        personMapper.updateEntityFromDto(updatePersonDto, person);
        return personMapper.toDto(personRepository.save(person));
    }

    @Transactional(readOnly = true)
    public PersonDto findPersonById(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
        return personMapper.toDto(person);
    }


    @Transactional(readOnly = true)
    public Page<PersonDto> findAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable)
                .map(personMapper::toDto);
    }

    @Transactional
    public void deletePerson(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        personRepository.delete(person);
        eventPublisher.publishEvent(new RecordDeletedEvent(this, personId, "Person"));
    }

    @Transactional(readOnly = true)
    public List<PersonDto> findAllPersonsById(List<Long> ids) {
        return personRepository.findAllById(ids).stream()
                .map(personMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<Person> findAllPersonsEntities(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Person> findModifiedSince(Instant since, Pageable pageable) {
        return personRepository.findModifiedSince(since, pageable);
    }

}
