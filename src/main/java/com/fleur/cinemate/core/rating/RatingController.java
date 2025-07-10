package com.fleur.cinemate.core.rating;


import com.fleur.cinemate.core.rating.dto.CreateRatingDto;
import com.fleur.cinemate.core.rating.dto.RatingDto;
import com.fleur.cinemate.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    public ResponseEntity<Page<RatingDto>> getRatingsByUser(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "filmIds") List<Long> ids,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(ratingService.findRatingsByUserAndFilmIds(userId, ids, pageable));
    }
}
