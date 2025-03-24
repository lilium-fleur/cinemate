package com.fleur.cinemate.core.relations.filmActor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmActorId implements Serializable {

    private Long film;
    private Long actor;
}
