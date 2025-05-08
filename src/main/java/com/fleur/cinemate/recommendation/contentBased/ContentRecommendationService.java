package com.fleur.cinemate.recommendation.contentBased;

import com.fleur.cinemate.collection.CollectionService;
import com.fleur.cinemate.collection.item.CollectionItemService;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmService;
import com.fleur.cinemate.core.relations.filmGenre.FilmGenreService;
import com.fleur.cinemate.core.relations.filmPerson.FilmPersonService;
import com.fleur.cinemate.core.relations.filmPerson.model.FilmRole;
import com.fleur.cinemate.recommendation.dto.RecommendationDto;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userList.UserListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentRecommendationService {
    private final static int FILM_PAGE_SIZE = 500;
    private final UserListService userListService;
    private final CollectionItemService collectionItemService;
    private final CollectionService collectionService;
    private final FilmGenreService filmGenreService;
    private final FilmPersonService filmPersonService;
    private final FilmService filmService;
    private final UserProfileService userProfileService;

    public List<RecommendationDto> getRecommendationFilms(User user) {
        Map<Film, Double> recommendations = getRecommendation(user);
        List<RecommendationDto> recommendationDtos = new ArrayList<>();
        for (Map.Entry<Film, Double> entry : recommendations.entrySet()) {
            RecommendationDto recommendationDto = RecommendationDto.builder()
                    .film(entry.getKey())
                    .recommendationScore(entry.getValue())
                    .build();
            recommendationDtos.add(recommendationDto);
        }
        return recommendationDtos;
    }

    private Map<Film, Double> getRecommendation(User user) {
        Map<String, Double> profile = userProfileService.buildUserProfile(user);
        Map<Film, Double> recommendations = new HashMap<>();

        Pageable pageable = PageRequest.ofSize(FILM_PAGE_SIZE);
        Set<Long> excludeFilmIds = getUsersFilmIds(user);
        Page<Film> films;
        do {
            films = filmService.findAllNotIn(excludeFilmIds, pageable);
            List<Long> filmIds = films.getContent().stream().map(Film::getId).toList();

            Map<Long, List<String>> genreNamesByFilm = filmGenreService
                    .findGenreNamesByFilms(filmIds);
            Map<Long, List<String>> actorNamesByFilm = filmPersonService
                    .findPersonNamesByFilmAndRole(filmIds, FilmRole.ACTOR);
            Map<Long, List<String>> directorNamesByFilm = filmPersonService
                    .findPersonNamesByFilmAndRole(filmIds, FilmRole.DIRECTOR);


            for (Film film : films) {
                recommendations.put(film, calculateFilmScore(
                        genreNamesByFilm.getOrDefault(film.getId(), List.of()),
                        actorNamesByFilm.getOrDefault(film.getId(), List.of()),
                        directorNamesByFilm.getOrDefault(film.getId(), List.of()),
                        profile));
            }

            recommendations = recommendations.entrySet().stream()
                    .filter(entry -> entry.getValue() > 0)
                    .sorted(Map.Entry.<Film, Double>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));

            pageable = pageable.next();

        } while (films.hasNext());

        return recommendations;
    }

    private Set<Long> getUsersFilmIds(User user) {
        Set<Long> fromLists = userListService.findDistinctFilmIdByUser(user.getId());
        Set<Long> fromCollections = collectionItemService.findDistinctItemsByCollectionIn(
                collectionService.findAllCollectionIdsByUser(user.getId()));
        fromLists.addAll(fromCollections);
        return fromLists;
    }


    private double calculateFilmScore(List<String> genres,
                                      List<String> actors,
                                      List<String> directors,
                                      Map<String, Double> profile) {
        double score = 0.0;
        int featureCount = 0;

        for (String genre : genres) {
            score += profile.getOrDefault(genre, 0.0);
            featureCount++;
        }

        for (String actor : actors) {
            score += profile.getOrDefault(actor, 0.0);
            featureCount++;
        }

        for (String director : directors) {
            score += profile.getOrDefault(director, 0.0);
            featureCount++;
        }

        if (featureCount == 0 || profile.isEmpty()) {
            return 0.0;
        }

        double minScore = 0.0;
        double maxScore = profile.values().stream()
                .limit(featureCount)
                .reduce(0.0, Double::sum);

        if (maxScore == 0.0) {
            return 0.0;
        }
        return (score - minScore) * 10 / (maxScore - minScore);
    }
}
