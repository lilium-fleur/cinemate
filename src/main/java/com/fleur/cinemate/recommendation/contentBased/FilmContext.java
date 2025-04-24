package com.fleur.cinemate.recommendation.contentBased;

import com.fleur.cinemate.core.film.Film;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilmContext {
    private Film film;
    private boolean inFavourite;
    private boolean inWatched;
    private boolean inWatchlist;
    private boolean inPersonalCollection;
    private Double ratingWeight;


    public double getMaxWeight(){
        double maxWeight = 0.0;
        if(inFavourite){
            maxWeight = Math.max(maxWeight, 1.0);
        }
        if(inPersonalCollection){
            maxWeight = Math.max(maxWeight, 0.8);
        }
        if(ratingWeight != null){
            maxWeight = Math.max(maxWeight, ratingWeight);
        }
        if(inWatched && ratingWeight == null){
            maxWeight = Math.max(maxWeight, 0.1);
        }
        if (inWatchlist) {
            maxWeight = Math.max(maxWeight, 0.1);
        }
        return maxWeight;
    }
}
