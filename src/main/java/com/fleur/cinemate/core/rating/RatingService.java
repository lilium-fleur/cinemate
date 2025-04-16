package com.fleur.cinemate.core.rating;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmRepository;
import com.fleur.cinemate.core.rating.dto.CreateRatingDto;
import com.fleur.cinemate.core.rating.dto.RatingDto;
import com.fleur.cinemate.event.RatingCreatedEvent;
import com.fleur.cinemate.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final FilmRepository filmRepository;
    private final RatingMapper ratingMapper;
    private final ApplicationEventPublisher eventPublisher;

    @CacheEvict(cacheNames = {"avgRatings"}, key = "#user.id")
    @Transactional
    public RatingDto createRating(CreateRatingDto createRatingDto, User user) {
        Film film = filmRepository.findById(createRatingDto.filmId())
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));
        ratingRepository.findByFilmIdAndUserId(film.getId(), user.getId())
                .ifPresent(rating -> {
                    throw new BadRequestException("Rating already exists");
                });
        Rating rating = Rating.builder()
                .user(user)
                .film(film)
                .rating(createRatingDto.rating())
                .build();
        RatingDto savedRatingDto = ratingMapper.toDto(ratingRepository.save(rating));

        eventPublisher.publishEvent(new RatingCreatedEvent(this, user.getId()));

        return savedRatingDto;
    }

    @Transactional(readOnly = true)
    public List<Rating> findByUserId(Long userId) {
        return ratingRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<Long> findDistinctUserIds(Pageable pageable) {
        return ratingRepository.findDistinctByUserIds(pageable);
    }

    @Transactional(readOnly = true)
    public Double findAverageRatingByUserId(Long userId) {
        return ratingRepository.findAverageRatingByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<Rating> findRatingsByFilmAndUsers(Long filmId, List<Long> users, Pageable pageable) {
        return ratingRepository.findByFilmIdAndUserIdIn(filmId, users, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Rating> findRatingsExcludeFilms(List<Long> excludeIds, Pageable pageable) {
        return ratingRepository.findByFilmIdNotIn(excludeIds, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Rating> findRatingsIncludeFilms(List<Long> includeIds, Pageable pageable) {
        return ratingRepository.findByFilmIdIn(includeIds, pageable);
    }
}
