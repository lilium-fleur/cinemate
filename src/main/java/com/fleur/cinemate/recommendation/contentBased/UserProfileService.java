package com.fleur.cinemate.recommendation.contentBased;

import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.collection.CollectionService;
import com.fleur.cinemate.collection.item.CollectionItem;
import com.fleur.cinemate.collection.item.CollectionItemService;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.rating.RatingService;
import com.fleur.cinemate.core.relations.filmGenre.FilmGenreService;
import com.fleur.cinemate.core.relations.filmPerson.FilmPersonService;
import com.fleur.cinemate.core.relations.filmPerson.model.FilmRole;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userList.UserListService;
import com.fleur.cinemate.userList.UserListType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private static final int COLLECTIONS_PAGE_SIZE = 50;
    private static final int ITEMS_PAGE_SIZE = 100;
    private static final int MAX_PROFILE_ENTRIES = 300;
    private static final int FILM_IDS_BATCH_SIZE = 500;

    @Cacheable(value = "userProfile", key = "#user")
    public Map<String, Double> buildUserProfile(User user) {
        Map<String, Double> profile = new HashMap<>();
        Map<Film, FilmContext> filmContexts = new HashMap<>();

        processFavoriteFilms(user, filmContexts);
        processWatchedFilms(user, filmContexts);
        processWatchlistFilms(user, filmContexts);
        processCollectionFilms(user, filmContexts);

        List<Long> filmIds = filmContexts.keySet().stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        int batchSize = FILM_IDS_BATCH_SIZE;

        for (int i = 0; i < filmIds.size(); i += batchSize) {
            List<Long> batchIds = filmIds.subList(i, Math.min(i + batchSize, filmIds.size()));
            Set<Long> batchIdSet = new HashSet<>(batchIds);

            Map<Long, List<String>> genreNamesByFilm = filmGenreService.findGenreNamesByFilms(batchIds);
            Map<Long, List<String>> actorNamesByFilm = filmPersonService
                    .findPersonNamesByFilmAndRole(batchIds, FilmRole.ACTOR);
            Map<Long, List<String>> directorNamesByFilm = filmPersonService
                    .findPersonNamesByFilmAndRole(batchIds, FilmRole.DIRECTOR);

            for (FilmContext filmContext : filmContexts.values()) {
                Long filmId = filmContext.getFilm().getId();
                if (batchIdSet.contains(filmId)) {
                    addFilmFeaturesToProfile(
                            profile,
                            genreNamesByFilm.getOrDefault(filmId, List.of()),
                            actorNamesByFilm.getOrDefault(filmId, List.of()),
                            directorNamesByFilm.getOrDefault(filmId, List.of()),
                            filmContext.getMaxWeight());
                }
            }
        }

        return profile.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(MAX_PROFILE_ENTRIES)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

    }


    private void processCollectionFilms(User user, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(COLLECTIONS_PAGE_SIZE);
        Page<Collection> userCollections;
        do {
            userCollections = collectionService
                    .findAllCollectionsByUser(user, pageable);
            for (Collection collection : userCollections.getContent()) {
                processCollectionItems(user, collection.getId(), filmContexts);
            }
            pageable = pageable.next();
        } while (userCollections.hasNext());
    }

    private void processCollectionItems(User user, Long collectionId, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(ITEMS_PAGE_SIZE);
        Page<CollectionItem> items;
        do {
            items = collectionItemService
                    .findItemsByCollection(collectionId, user, pageable);
            for (CollectionItem collectionItem : items.getContent()) {
                filmContexts.computeIfAbsent(collectionItem.getFilm(), film -> FilmContext.builder()
                                .film(collectionItem.getFilm())
                                .build())
                        .setInPersonalCollection(true);
            }
            pageable = pageable.next();
        } while (items.hasNext());
    }

    private void processWatchedFilms(User user, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(ITEMS_PAGE_SIZE);
        Page<Film> watchedFilms;
        do {
            watchedFilms = userListService.findFilmsByTypeList(user, UserListType.WATCHED, pageable);
            Map<Long, Double> ratingsByFilm = ratingService.findByFilmIdsAndUserId(
                    watchedFilms.map(Film::getId).toSet(), user.getId());

            for (Film film : watchedFilms.getContent()) {
                double rating = ratingsByFilm.getOrDefault(film.getId(), 0.0);

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
            pageable = pageable.next();
        } while (watchedFilms.hasNext());
    }

    private void processWatchlistFilms(User user, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(ITEMS_PAGE_SIZE);
        Page<Film> watchlistFilms;
        do {
            watchlistFilms = userListService.findFilmsByTypeList(user, UserListType.WATCHLIST, pageable);
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
            pageable = pageable.next();
        } while (watchlistFilms.hasNext());
    }

    private void processFavoriteFilms(User user, Map<Film, FilmContext> filmContexts) {
        Pageable pageable = PageRequest.ofSize(ITEMS_PAGE_SIZE);
        Page<Film> favoriteFilms;

        do {
            favoriteFilms = userListService.findFilmsByTypeList(user, UserListType.FAVORITE, pageable);
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
            pageable = pageable.next();
        } while (favoriteFilms.hasNext());
    }

    private double convertRatingToWeight(double rating) {
        return rating / 10.0;
    }

    private void addFilmFeaturesToProfile(Map<String, Double> profile,
                                          List<String> genreNames,
                                          List<String> actorNames,
                                          List<String> directorNames,
                                          double weight) {
        for (String genre : genreNames) {
            profile.merge(genre, weight, Double::sum);
        }
        for (String actor : actorNames) {
            profile.merge(actor, weight, Double::sum);
        }
        for (String director : directorNames) {
            profile.merge(director, weight, Double::sum);
        }
    }


}
