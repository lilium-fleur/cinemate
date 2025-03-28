package com.fleur.cinemate.search.service.query;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.fleur.cinemate.core.actor.ActorService;
import com.fleur.cinemate.core.actor.dto.ActorDto;
import com.fleur.cinemate.search.document.ActorDocument;
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
public class SearchActorService {
    private final ActorService actorService;
    private final ElasticsearchOperations elasticsearchOperations;


    public Page<ActorDto> findActorsByQuery(String query, Pageable pageable) {
        Query searchQuery;

        if(query != null && !query.isBlank()){
            searchQuery = MultiMatchQuery.of(m -> m
                    .query(query)
                    .fields("name")
                    .fuzziness("AUTO")
                    .prefixLength(1))
                    ._toQuery();
        } else {
            searchQuery = MatchAllQuery.of(m -> m)._toQuery();
        }

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(searchQuery)
                .withPageable(pageable)
                .build();

        return searchAndMap(nativeQuery, pageable);
    }

    private Page<ActorDto> searchAndMap(NativeQuery nativeQuery, Pageable pageable) {
        SearchHits<ActorDocument> hits = elasticsearchOperations.search(nativeQuery, ActorDocument.class);

        List<Long> actorIds = hits.stream()
                .map(hit -> hit.getContent().getId())
                .toList();

        Map<Long, ActorDto> actorsMap= actorService.findAllActorsById(actorIds).stream()
                .collect(Collectors.toMap(ActorDto::id, a -> a));

        List<ActorDto> result = actorIds.stream()
                .map(actorsMap::get)
                .toList();

        return new PageImpl<>(
                result,
                pageable,
                hits.getTotalHits());
    }
}
