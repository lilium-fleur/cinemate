package com.fleur.cinemate.core.rating;


import com.fleur.cinemate.core.rating.dto.CreateRatingDto;
import com.fleur.cinemate.core.rating.dto.RatingDto;
import com.fleur.cinemate.core.rating.dto.UpdateRatingDto;
import com.fleur.cinemate.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingDto> createRating(
            @RequestBody @Valid CreateRatingDto createRatingDto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ratingService.createRating(createRatingDto, user));
    }

    @PutMapping
    public ResponseEntity<RatingDto> updateRating(
            @RequestBody @Valid UpdateRatingDto updateRatingDto,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(ratingService.updateRating(updateRatingDto, user));
    }
}
