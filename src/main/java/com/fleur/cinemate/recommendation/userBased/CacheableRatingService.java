package com.fleur.cinemate.recommendation.userBased;

import com.fleur.cinemate.core.rating.Rating;
import com.fleur.cinemate.core.rating.RatingService;
import com.fleur.cinemate.recommendation.userBased.userSimilarity.UserSimilarity;
import com.fleur.cinemate.recommendation.userBased.userSimilarity.UserSimilarityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@Service
public class CacheableRatingService {
    private final RatingService ratingService;
    private final UserSimilarityService userSimilarityService;

    /**
     * Метод нужен для того, чтобы вычислить средний рейтинг пользователя для всех фильмов
     *
     * @param user Для кого среднее вычисляется
     * @return Средний рейтинг по всем фильмам
     */
    @Cacheable(value = "avgRatings", key = "#user")
    public Double getAvgRating(Long user) {
        Double avg = ratingService.findAverageRatingByUserId(user);
        return avg == null ? 0 : avg;
    }

    /**
     * Метод нужен для обновления данных в бд, а так же для обновления значений из кэша.
     * Запускает пересчет значения похожести для переданного юзера, со всеми похожими для него
     *
     * @param user Тот, для кого нужно пересчитать все похожести
     */
    @Async
    public void recomputeSimilaritiesAsync(Long user) {
        int page = 0;
        int size = 100;
        Page<Rating> allRatings;
        List<Long> userFilms = ratingService.findByUserId(user).stream()
                .map(rating -> rating.getFilm().getId())
                .toList();
        do {
            allRatings = ratingService.findRatingsIncludeFilms(userFilms, PageRequest.of(page, size));
            for (Rating rating : allRatings) {
                Long currentUserId = rating.getUser().getId();
                updateSimilarities(currentUserId, 20);
            }
            page++;
        } while (allRatings.hasNext());
    }

    /**
     * Метод нужен для обновления значений из кэша для юзеров - списков похожих юзеров
     * Так же запускает метод вычисления схожести и составления списка самых похожих юзеров
     * для переданного юзера, ограничивая первые n самых схожих
     *
     * @param mainUser     юзер для кого обновляется значение в кэше
     * @param countOfUsers число для ограничения количества похожих юзеров
     * @return список похожих на mainUser пользователей. Возвращает map с ключом - id похожего юзера,
     * значение - значение схожести этого юзера, с тем для кого проводится вычисление
     */
    @CachePut(value = "similarUsers", key = "{#mainUser, #countOfUsers}")
    public Map<Long, Double> updateSimilarities(Long mainUser, Integer countOfUsers) {
        return computeSimilarities(mainUser, countOfUsers);
    }

    /**
     * Метод нужен для внешнего вызова, с поддержкой кэширования значений.
     * Сначала проверяется есть ли уже записи в базе данных для пользователя,
     * если нет то запускается вычисление похожих пользователей и последующее сохранение в бд
     *
     * @param mainUser     тот для кого находятся похожие пользователи
     * @param countOfUsers ограничение по количеству похожих пользователей
     * @return список похожих на mainUser пользователей. Возвращает map с ключом - id похожего юзера,
     * значение - значение схожести этого юзера, с тем для кого проводится вычисление
     */
    @Cacheable(value = "similarUsers", key = "{#mainUser, #countOfUsers}", unless = "#result.isEmpty()")
    public Map<Long, Double> getSimilarUsers(Long mainUser, Integer countOfUsers) {
        List<UserSimilarity> similarities = userSimilarityService.findSimilaritiesByUser(mainUser);
        if (!similarities.isEmpty()) {
            Map<Long, Double> similaritiesMap = convertUserSimilaritiesToMap(mainUser, similarities);
            return similaritiesMap.entrySet().stream()
                    .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                    .limit(countOfUsers)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
        }
        return computeSimilarities(mainUser, countOfUsers);
    }

    /**
     * Метод для нахождения похожих пользователей и сохранения этих данных в бд
     *
     * @param mainUser     тот для кого находятся похожие пользователи
     * @param countOfUsers ограничение на количество похожих пользователей
     * @return список похожих на mainUser пользователей. Возвращает map с ключом - id похожего юзера,
     * значение - значение схожести этого юзера, с тем для кого проводится вычисление
     */
    private Map<Long, Double> computeSimilarities(Long mainUser, Integer countOfUsers) {
        int page = 0;
        int size = 100;
        Page<Long> allUsers;
        Map<Long, Double> usersSimilarities = new HashMap<>();
        do {
            allUsers = ratingService.findDistinctUserIds(PageRequest.of(page, size));
            for (Long user : allUsers) {
                if (!user.equals(mainUser)) {
                    usersSimilarities.put(user, calculateSimilarity(user, mainUser));
                }
            }
            page++;
        } while (allUsers.hasNext());

        usersSimilarities = usersSimilarities.entrySet().stream()
                .filter(entry -> entry.getValue() != null
                        && entry.getValue() != 0.0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        LinkedHashMap<Long, Double> resultSimilarUsers = usersSimilarities.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(countOfUsers)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        for (Map.Entry<Long, Double> entry : resultSimilarUsers.entrySet()) {
            userSimilarityService.saveSimilarity(mainUser, entry.getKey(), entry.getValue());
        }
        return resultSimilarUsers;
    }


    private Map<Long, Double> convertUserSimilaritiesToMap(Long mainUser, List<UserSimilarity> similarities) {
        Map<Long, Double> similaritiesMap = new HashMap<>();
        for (UserSimilarity userSimilarity : similarities) {
            if (userSimilarity.getUser2().getId().equals(mainUser)) {
                similaritiesMap.put(userSimilarity.getUser1().getId(), userSimilarity.getSimilarity());
            } else if (userSimilarity.getUser1().getId().equals(mainUser)) {
                similaritiesMap.put(userSimilarity.getUser2().getId(), userSimilarity.getSimilarity());
            }
        }
        return similaritiesMap;
    }

    /**
     * Метод вычисляет значение похожести для двух пользователей.
     * Вычисление по формуле корреляции Пирсона
     *
     * @return значение похожести между пользователями
     */
    private Double calculateSimilarity(Long user1, Long user2) {
        List<Rating> user1Ratings = ratingService.findByUserId(user1);
        List<Rating> user2Ratings = ratingService.findByUserId(user2);

        Map<Long, Double> user1RatingsMap = user1Ratings.stream()
                .collect(Collectors.toMap(rating -> rating.getFilm().getId(), Rating::getRating));
        Map<Long, Double> user2RatingsMap = user2Ratings.stream()
                .collect(Collectors.toMap(rating -> rating.getFilm().getId(), Rating::getRating));

        Set<Long> commonFilms = new HashSet<>(user1RatingsMap.keySet());
        commonFilms.retainAll(user2RatingsMap.keySet());

        if (commonFilms.isEmpty()) {
            log.debug("No common films between users: user id: {} and user id: {}", user1, user2);
            return 0.0;
        }

        commonFilms.removeIf(filmId ->
                user1RatingsMap.get(filmId) == null || user2RatingsMap.get(filmId) == null
        );

        double avg1 = commonFilms.stream()
                .mapToDouble(user1RatingsMap::get)
                .average()
                .orElse(0.0);

        double avg2 = commonFilms.stream()
                .mapToDouble(user2RatingsMap::get)
                .average()
                .orElse(0.0);

        double sum = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Long filmId : commonFilms) {
            double score1 = user1RatingsMap.get(filmId);
            double score2 = user2RatingsMap.get(filmId);

            if (Double.isNaN(score1) || Double.isInfinite(score1) ||
                    Double.isNaN(score2) || Double.isInfinite(score2)) {
                continue;
            }

            double diff1 = score1 - avg1;
            double diff2 = score2 - avg2;

            sum += diff1 * diff2;
            norm1 += Math.pow(diff1, 2);
            norm2 += Math.pow(diff2, 2);
        }

        double denominator = Math.sqrt(norm1 * norm2);
        if (denominator == 0.0) {
            return 0.0;
        }

        return sum / denominator;
    }

}
