package com.fleur.cinemate.search.service.sync;

import com.fleur.cinemate.core.person.Person;
import com.fleur.cinemate.core.person.PersonService;
import com.fleur.cinemate.search.document.PersonDocument;
import com.fleur.cinemate.search.entity.IndexName;
import com.fleur.cinemate.search.repository.ESSyncDateRepository;
import com.fleur.cinemate.search.repository.PersonDocumentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Log4j2
@Service
public class SyncPersonService extends SyncService<Person> {
    private final PersonDocumentRepository personDocumentRepository;
    private final PersonService personService;

    public SyncPersonService(ESSyncDateRepository esSyncDateRepository,
                             PersonDocumentRepository personDocumentRepository,
                             PersonService personService) {
        super(esSyncDateRepository);
        this.personDocumentRepository = personDocumentRepository;
        this.personService = personService;
    }

    @Override
    protected void saveToIndex(Page<Person> entityPage) {
        for (Person person : entityPage) {
            try {
                personDocumentRepository.save(convertToDocument(person));
            } catch (Exception e) {
                log.error("Error saving person document with id {} to index: {}",
                        person.getId(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected IndexName getIndexName() {
        return IndexName.PERSONS;
    }

    @Override
    protected Page<Person> findAllEntities(Pageable pageable) {
        return personService.findAllPersonsEntities(pageable);
    }

    @Override
    protected Page<Person> findEntitiesSinceDate(Instant sinceDate, Pageable pageable) {
        return personService.findModifiedSince(sinceDate, pageable);
    }

    private PersonDocument convertToDocument(Person person) {
        return PersonDocument.builder()
                .id(person.getId())
                .name(person.getName())
                .build();
    }
}
