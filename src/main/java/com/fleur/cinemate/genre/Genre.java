package com.fleur.cinemate.genre;

import com.fleur.cinemate.__shared.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genres")
@Entity
public class Genre extends BaseEntity {

    private String name;
}
