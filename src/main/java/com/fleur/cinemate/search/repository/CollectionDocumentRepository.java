package com.fleur.cinemate.search.repository;

import com.fleur.cinemate.search.document.CollectionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CollectionDocumentRepository extends ElasticsearchRepository<CollectionDocument, Long> {
}
