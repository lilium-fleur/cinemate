package com.fleur.cinemate.search.service.query;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.fleur.cinemate.core.film.FilmService;
import com.fleur.cinemate.core.film.dto.FilmDto;
import com.fleur.cinemate.search.document.FilmDocument;
import com.fleur.cinemate.search.dto.FilmFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@Service
public class SearchFilmService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final FilmService filmService;
    private final Integer SIZE_OF_SUGGESTIONS = 4;


    public Page<FilmDto> findFilmsByFilters(FilmFilter filter, Pageable pageable) {
        System.out.println(filter.query());
        List<Query> filters = new ArrayList<>();
        Query searchQuery;

        if (filter.query() != null && !filter.query().trim().isEmpty()) {
            searchQuery = MultiMatchQuery.of(m -> m
                            .query(filter.query())
                            .fields("title^4",
                                    "description^2",
                                    "genres_search^3",
                                    "actors_search^3",
                                    "directors_search^3")
                            .fuzziness("AUTO")
                            .prefixLength(1))
                    ._toQuery();
            filters.add(searchQuery);
        } else {
            searchQuery = MatchAllQuery.of(m -> m)._toQuery();
        }

        if (filter.genres() != null && !filter.genres().isEmpty()) {
            Query genresFilter = TermsQuery.of(t -> t
                            .field("genres")
                            .terms(term -> term.value(filter.genres().stream()
                                    .map(FieldValue::of)
                                    .toList())))
                    ._toQuery();
            filters.add(genresFilter);
        }

        if (filter.minRating() != null || filter.maxRating() != null) {
            Query ratingFilter = RangeQuery.of(r -> r.number(num -> num
                            .field("rating")
                            .gte(filter.minRating() != null ? filter.minRating() : null)
                            .lte(filter.maxRating() != null ? filter.maxRating() : null)))
                    ._toQuery();
            filters.add(ratingFilter);
        }

        if (filter.minYear() != null || filter.maxYear() != null) {
            Query yearFilter = RangeQuery.of(r -> r.number(num -> num
                            .field("releaseYear")
                            .gte(filter.minYear() != null ? filter.minYear().doubleValue() : null)
                            .lte(filter.maxYear() != null ? filter.maxYear().doubleValue() : null)))
                    ._toQuery();
            filters.add(yearFilter);
        }


        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q.bool(bool -> bool
                        .filter(filters)))
                .withSort(SortOptions.of(s -> s
                        .field(f -> f
                                .field(filter.sortBy().getValue())
                                .order(filter.sortBy().getValue().equals("title") ? SortOrder.Asc : SortOrder.Desc))))
                .withPageable(pageable)
                .build();

        return searchAndMap(nativeQuery, pageable);
    }

    public List<String> findSuggest(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            NativeQuery query = NativeQuery.builder()
                    .withQuery(MatchAllQuery.of(m -> m)._toQuery())
                    .withSuggester(Suggester.of(s -> s
                            .suggesters("film-suggestions", suggest -> suggest
                                    .prefix(prefix)
                                    .completion(c -> c
                                            .field("suggest")
                                            .skipDuplicates(true)
                                            .size(SIZE_OF_SUGGESTIONS)))))
                    .build();

            SearchHits<FilmDocument> hits = elasticsearchOperations.search(query, FilmDocument.class);
            Suggest suggest = hits.getSuggest();

            if (suggest == null) {
                return new ArrayList<>();
            }

            return suggest.getSuggestion("film-suggestions")
                    .getEntries()
                    .stream()
                    .flatMap(entry -> entry.getOptions().stream())
                    .map(option -> option.getText())
                    .toList();

        } catch (Exception e) {
            log.warn("Error requesting suggestions : {}", e.getMessage());
            return new ArrayList<>();
        }
    }


    private Page<FilmDto> searchAndMap(NativeQuery nativeQuery, Pageable pageable) {
        //достаем из индекса
        SearchHits<FilmDocument> hits = elasticsearchOperations.search(
                nativeQuery, FilmDocument.class, IndexCoordinates.of("films"));

        //для отладки
        hits.forEach(hit -> System.out.println("Score: " + hit.getScore() + ", Film: " + hit.getContent().getTitle()));

        //достаем айдишники найденых фильмов
        List<Long> filmIds = hits.stream()
                .map(hit -> hit.getContent().getId())
                .toList();

        //создаем мапу с фильмами и их айди
        Map<Long, FilmDto> filmsMap = filmService.findAllFilmsById(filmIds).stream()
                .collect(Collectors.toMap(FilmDto::id, film -> film));

        //устанавливаем тот же порядок что и при выдаче
        List<FilmDto> result = filmIds.stream()
                .map(filmsMap::get)
                .toList();

        //возвращаем страницу со списком фильмов, параметрами страницы, общим количеством результатов
        return new PageImpl<>(
                result,
                pageable,
                hits.getTotalHits());

    }
}
