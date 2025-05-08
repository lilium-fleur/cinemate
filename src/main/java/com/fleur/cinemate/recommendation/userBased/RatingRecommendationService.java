package com.fleur.cinemate.recommendation.userBased;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.rating.Rating;
import com.fleur.cinemate.core.rating.RatingService;
import com.fleur.cinemate.recommendation.dto.RecommendationDto;
import com.fleur.cinemate.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingRecommendationService {
    private final RatingService ratingService;
    private final CacheableRatingService cacheableRatingService;


    public Page<RecommendationDto> getRecommendFilms(User user, Pageable pageable) {
        Map<Film, Double> recommendationsMap = getRecommendations(user.getId());
        List<RecommendationDto> recommendationDtoList = new ArrayList<>();

        for (Map.Entry<Film, Double> entry : recommendationsMap.entrySet()) {
            RecommendationDto recommendationDto = RecommendationDto.builder()
                    .film(entry.getKey())
                    .recommendationScore(entry.getValue())
                    .build();
            recommendationDtoList.add(recommendationDto);
        }

        return new PageImpl<>(
                recommendationDtoList,
                pageable,
                recommendationDtoList.size());
    }

    /**
     * Метод находит фильмы для рекомендации пользователю
     *
     * @param user                    Пользователь для которого вычисляется рекомендованные фильмы
     * @return Map<Long, Double>, где Long - айди фильма для рекомендации, а Double - предсказанный рейтинг
     */
    private Map<Film, Double> getRecommendations(Long user) {
        List<Long> userFilms = gerRatedFilms(user);
        Double userAvgRating = cacheableRatingService.getAvgRating(user);
        Map<Long, Double> topSimilarUsers = cacheableRatingService.getSimilarUsers(user, 20);

        int page = 0;
        int size = 100;
        Page<Rating> ratings;
        Map<Film, Double> recommendationsFilms = new HashMap<>();

        do {
            ratings = ratingService.findRatingsExcludeFilms(userFilms, PageRequest.of(page, size));
            for (Rating rating : ratings.getContent()) {
                Double predictedRating = predictRating(user, rating.getFilm().getId(), topSimilarUsers);
                recommendationsFilms.put(rating.getFilm(), predictedRating);
            }
            page++;
        } while (ratings.hasNext());

        recommendationsFilms = recommendationsFilms.entrySet().stream()
                .filter(e -> e.getValue() >= userAvgRating - 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return recommendationsFilms;
    }


    private List<Long> gerRatedFilms(Long user) {
        return ratingService.findByUserId(user).stream()
                .map(rating -> rating.getFilm().getId())
                .toList();
    }


    /**
     * Метод предсказывает рейтинг для фильма используя для вычислений оценки похожих пользователей
     * и ранжируя по степени похожести - оценка более похожего пользователя влияет больше
     *
     * @param user            Пользователь чей рейтинг предсказывается
     * @param film            Фильм, для которого предсказывается рейтинг
     * @param topSimilarUsers список, похожих юзеров для предсказания рейтинга по их оценкам этого фильма
     * @return Предсказанный рейтинг user для film
     */
    private Double predictRating(Long user, Long film, Map<Long, Double> topSimilarUsers) {
        Double avgRatingUser = cacheableRatingService.getAvgRating(user);
        Map<Long, Double> avgRatings = cacheableRatingService.getSimilarUsers(user, 20).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> cacheableRatingService.getAvgRating(entry.getKey()
                        )));

        int page = 0;
        int size = 1000;
        Page<Rating> ratings;
        double numerator = 0.0;
        double denominator = 0.0;
        do {
            ratings = ratingService.findRatingsByFilmAndUsers(film, topSimilarUsers.keySet().stream().toList(), PageRequest.of(page, size));
            for (Rating rating : ratings.getContent()) {
                Long otherUser = rating.getUser().getId();
                double similarity = topSimilarUsers.get(otherUser);
                double avgRatingSimilarUser = avgRatings.get(otherUser);
                double ratingSimilarUser = rating.getRating();
                numerator += similarity * (ratingSimilarUser - avgRatingSimilarUser);
                denominator += Math.abs(similarity);
            }
            page++;
        } while (ratings.hasNext());

        return denominator == 0 ? avgRatingUser : avgRatingUser + (numerator / denominator);
    }


}
