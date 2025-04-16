package com.fleur.cinemate.search.document;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Document(indexName = "films")
@Setting(replicas = 0)
@Mapping(mappingPath = "elasticsearch/film-suggest-mapping.json")
public class FilmDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "english")
    private String title;

    @Field(type = FieldType.Text, analyzer = "english")
    private String description;

    @Field(type = FieldType.Integer)
    private Integer releaseYear;

    @Field(type = FieldType.Double)
    private Double rating;

    @Field(type = FieldType.Object)
    private Completion suggest;

    @Field(type = FieldType.Keyword, normalizer = "lowercase")
    @Builder.Default
    private Set<String> genres = new HashSet<>();

    @Field(type = FieldType.Keyword, normalizer = "lowercase")
    @Builder.Default
    private Set<String> actors = new HashSet<>();

    @Field(type = FieldType.Keyword, normalizer = "lowercase")
    @Builder.Default
    private Set<String> directors = new HashSet<>();

    @Field(type = FieldType.Text, name = "genres_search", analyzer = "english")
    private String genresSearch;

    @Field(type = FieldType.Text, name = "actors_search", analyzer = "english")
    private String actorsSearch;

    @Field(type = FieldType.Text, name = "actors_search", analyzer = "english")
    private String directorsSearch;
}
