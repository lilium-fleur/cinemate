package com.fleur.cinemate.filmGenre;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmGenreId implements Serializable {

    private Long filmId;

    private Long genreId;
}
