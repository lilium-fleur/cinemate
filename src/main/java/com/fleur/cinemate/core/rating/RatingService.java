package com.fleur.cinemate.core.rating;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmRepository;
import com.fleur.cinemate.core.rating.dto.CreateRatingDto;
import com.fleur.cinemate.core.rating.dto.RatingDto;
import com.fleur.cinemate.core.rating.dto.UpdateRatingDto;
import com.fleur.cinemate.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final FilmRepository filmRepository;
    private final RatingMapper ratingMapper;


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
                .score(createRatingDto.score())
                .build();
        return ratingMapper.toDto(ratingRepository.save(rating));
    }

    @Transactional
    public RatingDto updateRating(UpdateRatingDto updateRatingDto, User user) {
        Film film = filmRepository.findById(updateRatingDto.filmId())
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));
        Rating rating = ratingRepository.findByFilmIdAndUserId(film.getId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));
        rating.setScore(updateRatingDto.score());
        return ratingMapper.toDto(ratingRepository.save(rating));
    }

}
