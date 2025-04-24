package com.fleur.cinemate.recommendation.userBased;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/recommendations/users")
public class RatingRecommendationController {
    private final RatingRecommendationService ratingRecommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<Map<Long, Double>> getRecommendations(
            @PathVariable Long userId) {
        return ResponseEntity
                .ok(ratingRecommendationService.getRecommendations(userId, 20));
    }

}
