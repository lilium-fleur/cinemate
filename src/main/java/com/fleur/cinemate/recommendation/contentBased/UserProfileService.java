package com.fleur.cinemate.recommendation.contentBased;

import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.collection.CollectionService;
import com.fleur.cinemate.collection.item.CollectionItem;
import com.fleur.cinemate.collection.item.CollectionItemService;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.rating.Rating;
import com.fleur.cinemate.core.rating.RatingService;
import com.fleur.cinemate.core.relations.filmGenre.FilmGenreService;
import com.fleur.cinemate.core.relations.filmPerson.FilmPersonService;
import com.fleur.cinemate.core.relations.filmPerson.Role;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userList.UserListService;
import com.fleur.cinemate.userList.UserListType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final CollectionItemService collectionItemService;
    private final UserListService userListService;
    private final CollectionService collectionService;
    private final FilmGenreService filmGenreService;
    private final FilmPersonService filmPersonService;
    private final RatingService ratingService;
    
    public Map<String, Double> buildUserProfile(User user) {

        Map<String, Double> profile = new HashMap<>();
        Map<Film, FilmContext> filmContexts = new HashMap<>();

        processFavoriteFilms(user, filmContexts);
        processWatchedFilms(user, filmContexts);
        processWatchlistFilms(user, filmContexts);
        processCollectionFilms(user, filmContexts);

        for (FilmContext filmContext : filmContexts.values()) {
            addFilmFeaturesToProfile(profile, filmContext.getFilm(), filmContext.getMaxWeight());
        }
        return profile.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(300)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }


    private void processCollectionFilms(User user, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(50);
        Page<Collection> userCollections = collectionService
                .findAllCollectionsByUser(user,pageable);
        do {
            for (Collection collection : userCollections.getContent()) {
                processCollectionItems(user, collection.getId(), filmContexts);
            }
        } while (userCollections.hasNext());
    }

    private void processCollectionItems(User user, Long collectionId, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(100);
        Page<CollectionItem> items = collectionItemService
                .findItemsByCollection(collectionId, user, pageable);
        do {
            for (CollectionItem collectionItem : items.getContent()) {
                if (filmContexts.containsKey(collectionItem.getFilm())) {
                    filmContexts.get(collectionItem.getFilm()).setInPersonalCollection(true);
                } else {
                    FilmContext filmContext = FilmContext.builder()
                            .film(collectionItem.getFilm())
                            .inPersonalCollection(true)
                            .build();
                    filmContexts.put(collectionItem.getFilm(), filmContext);
                }
            }
            pageable.next();
        } while(items.hasNext());
    }

    private void processWatchedFilms(User user, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(100);
        Page<Film> watchedFilms = userListService.findFilmsByTypeList(user, UserListType.WATCHED, pageable);

        do {
            for (Film film : watchedFilms.getContent()) {
                double rating = ratingService.findByFilmIdAndUserId(film.getId(), user.getId())
                        .map(Rating::getRating)
                        .orElse(0.0);
                if (filmContexts.containsKey(film)) {
                    FilmContext context = filmContexts.get(film);
                    context.setInWatched(true);
                    context.setRatingWeight(rating > 0.0 ? convertRatingToWeight(rating) : null);
                } else {
                    FilmContext filmContext = FilmContext.builder()
                            .film(film)
                            .inWatched(true)
                            .ratingWeight(rating > 0.0 ? convertRatingToWeight(rating) : null)
                            .build();
                    filmContexts.put(film, filmContext);
                }
            }
        } while (watchedFilms.hasNext());
    }

    private void processWatchlistFilms(User user, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(100);
        Page<Film> watchlistFilms = userListService.findFilmsByTypeList(user, UserListType.WATCHLIST, pageable);
        do {
            for (Film film : watchlistFilms.getContent()) {
                if (filmContexts.containsKey(film)) {
                    filmContexts.get(film).setInWatchlist(true);
                } else {
                    FilmContext filmContext = FilmContext.builder()
                            .film(film)
                            .inWatchlist(true)
                            .build();
                    filmContexts.put(film, filmContext);
                }
            }
            pageable.next();
        } while (watchlistFilms.hasNext());
    }

    private void processFavoriteFilms(User user, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(100);
        Page<Film> favoriteFilms = userListService.findFilmsByTypeList(user, UserListType.FAVORITE, pageable);

        do {
            for (Film film : favoriteFilms.getContent()) {
                if (filmContexts.containsKey(film)) {
                    filmContexts.get(film).setInFavourite(true);
                } else {
                    FilmContext filmContext = FilmContext.builder()
                            .film(film)
                            .inFavourite(true)
                            .build();
                    filmContexts.put(film, filmContext);
                }
            }
            pageable.next();
        } while (favoriteFilms.hasNext());
    }

    private double convertRatingToWeight(double rating) {
        return rating / 10.0;
    }

    private void addFilmFeaturesToProfile(Map<String, Double> profile, Film film, double weight) {
        List<String> genres = filmGenreService.findAllGenreNamesByFilm(film.getId(), Pageable.unpaged());
        for (String genre : genres) {
            profile.merge(genre, weight, Double::sum);
        }
        List<String> actors = filmPersonService.findPersonNamesByFilmAndRole(film.getId(), Role.ACTOR);
        for (String actor : actors) {
            profile.merge(actor, weight, Double::sum);
        }
        List<String> directors = filmPersonService.findPersonNamesByFilmAndRole(film.getId(), Role.DIRECTOR);
        for (String director : directors) {
            profile.merge(director, weight, Double::sum);
        }
    }


}
