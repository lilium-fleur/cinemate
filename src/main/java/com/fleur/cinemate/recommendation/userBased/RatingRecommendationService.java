package com.fleur.cinemate.recommendation.userBased;

import com.fleur.cinemate.core.rating.Rating;
import com.fleur.cinemate.core.rating.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingRecommendationService {
    private final RatingService ratingService;
    private final CacheableRatingService cacheableRatingService;


    /**
     * Метод находит фильмы для рекомендации пользователю
     *
     * @param user                    Пользователь для которого вычисляется рекомендованные фильмы
     * @param numberOfRecommendations Количество выдаваемых рекомендованных фильмов
     * @return Map<Long, Double>, где Long - айди фильма для рекомендации, а Double - предсказанный рейтинг
     */
    public Map<Long, Double> getRecommendations(Long user, Integer numberOfRecommendations) {
        List<Long> userFilms = gerRatedFilms(user);
        Double userAvgRating = cacheableRatingService.getAvgRating(user);
        Map<Long, Double> topSimilarUsers = cacheableRatingService.getSimilarUsers(user, 20);

        int page = 0;
        int size = 1000;
        Page<Rating> ratings;
        Map<Long, Double> recommendationsFilms = new HashMap<>();

        do {
            ratings = ratingService.findRatingsExcludeFilms(userFilms, PageRequest.of(page, size));
            for (Rating rating : ratings.getContent()) {
                Double predictedRating = predictRating(user, rating.getFilm().getId(), topSimilarUsers);
                recommendationsFilms.put(rating.getFilm().getId(), predictedRating);
            }
            page++;
        } while (ratings.hasNext());

        recommendationsFilms = recommendationsFilms.entrySet().stream()
                .filter(e -> e.getValue() >= userAvgRating)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return recommendationsFilms.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(numberOfRecommendations)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
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
