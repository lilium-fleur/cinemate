package com.fleur.cinemate.search.repository;

import com.fleur.cinemate.search.document.FilmDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface FilmDocumentRepository extends ElasticsearchRepository<FilmDocument, Long> {

}
