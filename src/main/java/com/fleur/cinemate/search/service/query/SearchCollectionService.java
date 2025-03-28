package com.fleur.cinemate.search.service.query;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.fleur.cinemate.collection.CollectionService;
import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.search.document.CollectionDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchCollectionService {
    private final CollectionService collectionService;
    private final ElasticsearchOperations elasticsearchOperations;

    public Page<CollectionDto> findCollectionsByQuery(String query, Pageable pageable) {
        Query searchQuery;

        if (query != null && !query.trim().isEmpty()) {
            searchQuery = MultiMatchQuery.of(m -> m
                            .fields("title", "description")
                            .fuzziness("AUTO")
                            .prefixLength(1))
                    ._toQuery();
        } else{
            searchQuery = MatchAllQuery.of(m -> m)._toQuery();
        }

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(searchQuery)
                .withPageable(pageable)
                .build();

        return searchAndMap(nativeQuery, pageable);
    }

    private Page<CollectionDto> searchAndMap(NativeQuery nativeQuery, Pageable pageable) {
        SearchHits<CollectionDocument> hits = elasticsearchOperations.search(nativeQuery, CollectionDocument.class);

        List<Long> collectionIds = hits.stream()
                .map(hit -> hit.getContent().getId())
                .toList();

        Map<Long, CollectionDto> collectionsMap = collectionService.findAllCollectionById(collectionIds).stream()
                .collect(Collectors.toMap(CollectionDto::id, c -> c));

        List<CollectionDto> result = collectionIds.stream()
                .map(collectionsMap::get)
                .toList();

        return new PageImpl<>(
                result,
                pageable,
                hits.getTotalHits());
    }
}
