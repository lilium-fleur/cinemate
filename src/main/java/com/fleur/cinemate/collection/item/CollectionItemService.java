package com.fleur.cinemate.collection.item;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.collection.CollectionService;
import com.fleur.cinemate.collection.item.dto.CollectionItemDto;
import com.fleur.cinemate.collection.item.dto.CreateCollectionItemDto;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmRepository;
import com.fleur.cinemate.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollectionItemService {

    private final CollectionItemRepository collectionItemRepository;
    private final CollectionService collectionService;
    private final CollectionItemMapper collectionItemMapper;
    private final FilmRepository filmRepository;

    @Transactional
    public CollectionItemDto addItemToCollection(
            CreateCollectionItemDto createCollectionItemDto,
            Long collectionId,
            User currentUser) {

        collectionItemRepository.findByCollectionIdAndFilmId(collectionId, createCollectionItemDto.filmId())
                .ifPresent(item -> {
                    throw new BadRequestException("This film already added");
                });

        Collection collection = collectionService.getOrThrowException(
                collectionId, currentUser);

        Film film = filmRepository.findById(createCollectionItemDto.filmId())
                .orElseThrow(() -> new EntityNotFoundException("Film not found"));


        CollectionItem collectionItem = CollectionItem.builder()
                .collection(collection)
                .film(film)
                .position(calculatePosition(collection))
                .build();

        return collectionItemMapper.toDto(collectionItemRepository.save(collectionItem));
    }

    @Transactional
    public void removeItemByCollection(Long collectionId, Long itemId, User currentUser) {
        collectionService.getOrThrowException(collectionId, currentUser);
        CollectionItem item = collectionItemRepository
                .findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Film was not added to this collection"));

        collectionItemRepository.delete(item);
    }

    @Transactional
    public CollectionItemDto changePositionCollectionItem(Long filmCollectionId,
                                                          User currentUser,
                                                          Long itemId,
                                                          Integer targetPosition) {

        Collection collection = collectionService.getOrThrowException(filmCollectionId, currentUser);

        CollectionItem item = collectionItemRepository
                .findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Collection item not found"));

        Integer currentPosition = item.getPosition();
        Integer maxPosition = collectionItemRepository.findMaxPositionByFilmCollection(collection);

        if(Objects.equals(targetPosition, currentPosition)) {
            return collectionItemMapper.toDto(item);
        }
        //validate target position
        if(targetPosition > maxPosition + 1) targetPosition = maxPosition + 1;
        if(targetPosition < 1) targetPosition = 1;

        //shift items position
        if(currentPosition < targetPosition){
            if(targetPosition <= maxPosition){
                shiftPositionDown(collection, currentPosition + 1, targetPosition);
            }
        } else {
            shiftPositionUp(collection, targetPosition, currentPosition - 1);
        }

        //change position of selected item
        item.setPosition(targetPosition);
        return collectionItemMapper.toDto(collectionItemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public Page<CollectionItemDto> findItemsDtoByCollection(
            Long filmCollectionId,
            User currentUser,
            Pageable pageable) {

        Long collectionId = collectionService.findCollectionById(filmCollectionId, currentUser).id();

        return collectionItemRepository.findByCollectionIdOrderByPosition(collectionId, pageable)
                .map(collectionItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<CollectionItem> findItemsByCollection(
            Long filmCollectionId,
            User currentUser,
            Pageable pageable) {

        Long collectionId = collectionService.findCollectionById(filmCollectionId, currentUser).id();
        return collectionItemRepository.findByCollectionIdOrderByPosition(collectionId, pageable);
    }

    @Transactional(readOnly = true)
    public Set<Long> findDistinctItemsByCollectionIn(List<Long> collectionIds) {
        return collectionItemRepository.findDistinctByCollectionIdIn(collectionIds);
    }

    @Transactional(readOnly = true)
    public List<Long> findFilmIdsByCollection(
            Long collectionId){
        return collectionItemRepository.findByCollectionIdOrderByPosition(collectionId, Pageable.unpaged())
                .map(collectionItem -> collectionItem.getFilm().getId())
                .toList();
    }


    private void shiftPositionDown(Collection collection, Integer startPosition, Integer endPosition) {
        collectionItemRepository.shiftPositionDownByFilmCollection(collection, startPosition, endPosition);
    }

    private void shiftPositionUp(Collection collection, Integer startPosition, Integer endPosition) {
        collectionItemRepository.shiftPositionUpByFilmCollection(collection, startPosition, endPosition);
    }

    private Integer calculatePosition(Collection collection){
        Integer maxPosition = collectionItemRepository
                .findMaxPositionByFilmCollection(collection);
        if (Objects.isNull(maxPosition)) return 1;
        return maxPosition + 1;
    }

}
