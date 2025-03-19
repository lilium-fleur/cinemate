package com.fleur.cinemate.core.ralations.filmGenre;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmGenreId implements Serializable {

    private Long film;

    private Long genre;
}
