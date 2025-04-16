package com.fleur.cinemate.search.service.query;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.fleur.cinemate.core.person.PersonService;
import com.fleur.cinemate.core.person.dto.PersonDto;
import com.fleur.cinemate.search.document.PersonDocument;
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
public class SearchPersonService {
    private final PersonService personService;
    private final ElasticsearchOperations elasticsearchOperations;


    public Page<PersonDto> findActorsByQuery(String query, Pageable pageable) {
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

    private Page<PersonDto> searchAndMap(NativeQuery nativeQuery, Pageable pageable) {
        SearchHits<PersonDocument> hits = elasticsearchOperations.search(nativeQuery, PersonDocument.class);

        List<Long> actorIds = hits.stream()
                .map(hit -> hit.getContent().getId())
                .toList();

        Map<Long, PersonDto> actorsMap = personService.findAllPersonsById(actorIds).stream()
                .collect(Collectors.toMap(PersonDto::id, a -> a));

        List<PersonDto> result = actorIds.stream()
                .map(actorsMap::get)
                .toList();

        return new PageImpl<>(
                result,
                pageable,
                hits.getTotalHits());
    }
}
