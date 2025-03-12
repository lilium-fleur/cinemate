package com.fleur.cinemate.userCollection.collection.filmCollectionItem;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.film.Film;
import com.fleur.cinemate.film.FilmService;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userCollection.collection.filmCollection.FilmCollection;
import com.fleur.cinemate.userCollection.collection.filmCollection.FilmCollectionService;
import com.fleur.cinemate.userCollection.collection.filmCollection.dto.FilmCollectionDto;
import com.fleur.cinemate.userCollection.collection.filmCollectionItem.dto.CreateFilmCollectionItemDto;
import com.fleur.cinemate.userCollection.collection.filmCollectionItem.dto.FilmCollectionItemDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FilmCollectionItemService {

    private final FilmCollectionItemRepository filmCollectionItemRepository;
    private final FilmCollectionService filmCollectionService;
    private final FilmService filmService;
    private final FilmCollectionItemMapper filmCollectionItemMapper;

    @Transactional
    public FilmCollectionItemDto addItemToCollection(
            CreateFilmCollectionItemDto createFilmCollectionItemDto,
            Long filmCollectionId,
            User currentUser) {

        FilmCollection filmCollection = filmCollectionService.getOrThrowException(
                filmCollectionId, currentUser);
        Film film = filmService.findFilmEntityById(createFilmCollectionItemDto.film_id());

        filmCollectionItemRepository.findByFilmCollectionAndFilm(filmCollection, film)
                .ifPresent(filmCollectionItem -> {
                    throw new BadRequestException("This film already added");
                });
        FilmCollectionItem filmCollectionItem = FilmCollectionItem.builder()
                .filmCollection(filmCollection)
                .film(film)
                .position(calculatePosition(filmCollection))
                .build();
        return filmCollectionItemMapper.toDto(filmCollectionItemRepository.save(filmCollectionItem));

    }

    @Transactional
    public void removeItemByCollection(Long filmCollectionId, Long FilmCollectionItemId, User currentUser) {
        filmCollectionService.getOrThrowException(filmCollectionId, currentUser);
        FilmCollectionItem item = filmCollectionItemRepository.findById(FilmCollectionItemId)
                .orElseThrow(() -> new EntityNotFoundException("Collection item not found"));
        filmCollectionItemRepository.delete(item);
    }

    @Transactional
    public FilmCollectionItemDto changePositionCollectionItem(Long filmCollectionId,
                                                              User currentUser,
                                                              Long filmCollectionItemId,
                                                              Integer targetPosition) {

        FilmCollection filmCollection = filmCollectionService.getOrThrowException(filmCollectionId, currentUser);
        FilmCollectionItem item = filmCollectionItemRepository.findById(filmCollectionItemId)
                .orElseThrow(() -> new EntityNotFoundException("Collection item not found"));
        Integer currentPosition = item.getPosition();
        Integer maxPosition = filmCollectionItemRepository.findMaxPositionByFilmCollection(filmCollection);

        if(Objects.equals(targetPosition, currentPosition)) {
            return filmCollectionItemMapper.toDto(item);
        }
        //validate target position
        if(targetPosition > maxPosition + 1) targetPosition = maxPosition + 1;
        if(targetPosition < 1) targetPosition = 1;

        //shift items position
        if(currentPosition < targetPosition){
            if(targetPosition <= maxPosition){
                shiftPositionDown(filmCollection, currentPosition + 1, targetPosition);
            }
        } else {
            shiftPositionUp(filmCollection, targetPosition, currentPosition - 1);
        }

        //change position of selected item
        item.setPosition(targetPosition);
        return filmCollectionItemMapper.toDto(filmCollectionItemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public Page<FilmCollectionItemDto> findItemsByFilmCollection(
            Long filmCollectionId,
            User currentUser,
            Pageable pageable) {

        FilmCollectionDto collectionDto = filmCollectionService.findFilmCollectionById(filmCollectionId, currentUser);

        return filmCollectionItemRepository.findByFilmCollectionIdOrderByPosition(collectionDto.id(), pageable)
                .map(filmCollectionItemMapper::toDto);
    }


    private void shiftPositionDown(FilmCollection filmCollection, Integer startPosition, Integer endPosition) {
        filmCollectionItemRepository.shiftPositionDownByFilmCollection(filmCollection, startPosition, endPosition);
    }

    private void shiftPositionUp(FilmCollection filmCollection, Integer startPosition, Integer endPosition) {
        filmCollectionItemRepository.shiftPositionUpByFilmCollection(filmCollection, startPosition, endPosition);
    }

    private Integer calculatePosition(FilmCollection filmCollection){
        Integer maxPosition = filmCollectionItemRepository
                .findMaxPositionByFilmCollection(filmCollection);

        return maxPosition + 1;
    }

}
