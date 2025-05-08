package com.fleur.cinemate.recommendation;

import com.fleur.cinemate.recommendation.contentBased.ContentRecommendationService;
import com.fleur.cinemate.recommendation.dto.RecommendationDto;
import com.fleur.cinemate.recommendation.userBased.RatingRecommendationService;
import com.fleur.cinemate.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/recommendations")
public class RecommendationController {
    private final RatingRecommendationService ratingRecommendationService;
    private final ContentRecommendationService contentRecommendationService;

    @GetMapping("/user-based")
    public ResponseEntity<Page<RecommendationDto>> getRecommendations(
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(ratingRecommendationService.getRecommendFilms(user, pageable));
    }

    @GetMapping("/content-based")
    public ResponseEntity<List<RecommendationDto>> getRecommendations(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(contentRecommendationService.getRecommendationFilms(user));
    }


}
