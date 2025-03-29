package com.fleur.cinemate.search.document;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Document(indexName = "actors")
@Setting(replicas = 0)
public class ActorDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "english")
    private String name;
}
