package com.fleur.cinemate.listener;

import com.fleur.cinemate.event.RatingCreatedEvent;
import com.fleur.cinemate.recommendation.userBased.CacheableRatingService;
import com.fleur.cinemate.recommendation.userBased.userSimilarity.UserSimilarityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Log4j2
@Component
public class RatingCreateEventListener {
    private final UserSimilarityService userSimilarityService;
    private final CacheableRatingService cacheableRatingService;

    @EventListener
    public void handleRatingCreated(RatingCreatedEvent event) {
        try {
            if (event == null || event.getUser() == null) {
                log.warn("RatingCreatedEvent is null or empty");
                return;
            }
            Long userId = event.getUser();
            userSimilarityService.deleteSimilarityByUser(userId);
            log.info("Similarities related to user: {} has been deleted", userId);
            cacheableRatingService.recomputeSimilaritiesAsync(userId);
            log.info("Similarities related to user: {} has been updated", userId);

        } catch (Exception e) {
            log.error("Error handling RatingCreatedEvent for user: {}, {}",
                    event != null ? event.getUser() : "null", e);
        }
    }


}
