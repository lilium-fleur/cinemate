package com.fleur.cinemate.search.document;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Document(indexName = "films")
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

    @Field(type = FieldType.Keyword, normalizer = "lowercase")
    @Builder.Default
    private Set<String> genres = new HashSet<>();

    @Field(type = FieldType.Keyword, normalizer = "lowercase")
    @Builder.Default
    private Set<String> actors = new HashSet<>();

    @Field(type = FieldType.Text, name = "genres_search", analyzer = "english")
    private String genresSearch;

    @Field(type = FieldType.Text, name = "actors_search", analyzer = "english")
    private String actorsSearch;

}
