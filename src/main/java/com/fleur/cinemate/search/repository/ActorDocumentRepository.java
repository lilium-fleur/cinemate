package com.fleur.cinemate.search.repository;

import com.fleur.cinemate.search.document.ActorDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorDocumentRepository extends ElasticsearchRepository<ActorDocument, Long> {
}
