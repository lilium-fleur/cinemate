package com.fleur.cinemate.search.repository;

import com.fleur.cinemate.search.document.PersonDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDocumentRepository extends ElasticsearchRepository<PersonDocument, Long> {
}
