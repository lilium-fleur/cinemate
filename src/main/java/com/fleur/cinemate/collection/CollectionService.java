package com.fleur.cinemate.collection;

import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.collection.dto.CollectionDtoWithSize;
import com.fleur.cinemate.collection.dto.CreateCollectionDto;
import com.fleur.cinemate.collection.dto.UpdateCollectionDto;
import com.fleur.cinemate.event.RecordDeletedEvent;
import com.fleur.cinemate.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionMapper collectionMapper;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public CollectionDto createCollection(
            CreateCollectionDto createCollectionDto, User user) {

        Collection collection = Collection.builder()
                .name(createCollectionDto.name())
                .description(createCollectionDto.description())
                .author(user)
                .isPublic(createCollectionDto.isPublic())
                .build();

        return collectionMapper.toDto(collectionRepository.save(collection));
    }

    @Transactional
    public CollectionDto updateCollection(
            UpdateCollectionDto updateCollectionDto, Long filmCollectionId, User user) {
        Collection collection = getOrThrowException(filmCollectionId, user);

        Collection updatedCollection = collectionMapper.updateEntityFromDto(updateCollectionDto, collection);

        return collectionMapper.toDto(collectionRepository.save(updatedCollection));
    }

    @Transactional
    public void deleteCollection(Long collectionId, User user) {
        Collection collection = getOrThrowException(collectionId, user);
        collectionRepository.delete(collection);

        eventPublisher.publishEvent(new RecordDeletedEvent(this, collectionId, "Collection"));
    }


    @Transactional(readOnly = true)
    public List<CollectionDto> findAllCollectionByIds(List<Long> ids) {
        return collectionRepository.findAllById(ids).stream()
                .map(collectionMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<Collection> findAllPublicCollectionEntities(Pageable pageable) {
        return collectionRepository.findAllPublic(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Collection> findModifiedSince(Instant since, Pageable pageable) {
        return collectionRepository.findModifiedSince(since, pageable);
    }

    @Transactional(readOnly = true)
    public Collection findPublicCollectionById(Long collectionId, Long userId) {
        Collection collection = collectionRepository.findPublicById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));

        if (!collection.getAuthor().getId().equals(userId) && !collection.getIsPublic()) {
            throw new AccessDeniedException("Permission denied");
        }
        return collection;
    }


    @Transactional(readOnly = true)
    public CollectionDtoWithSize findCollectionByIdWithSize(Long collectionId, User currentUser) {
        CollectionDtoWithSize collectionDto = collectionRepository.findPublicByIdWithSize(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));
        if (!collectionDto.getIsPublic() && !collectionDto.getAuthorId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission not found");
        }
        return collectionDto;
    }

    @Transactional(readOnly = true)
    public CollectionDto findCollectionById(Long collectionId, User currentUser) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));
        if (!collection.getIsPublic() && !collection.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission not found");
        }
        return collectionMapper.toDto(collection);
    }

    @Transactional(readOnly = true)
    public Page<CollectionDtoWithSize> findAllPublicCollection(Pageable pageable) {
        return collectionRepository.findAllPublicWithSize(pageable);
    }


    @Transactional(readOnly = true)
    public Page<Collection> findAllCollectionsByUser(User user, Pageable pageable) {
        return collectionRepository.findAllByAuthor(user, pageable);
    }

    @Transactional(readOnly = true)
    public List<Long> findAllCollectionIdsByUser(Long userId) {
        return collectionRepository.findCollectionIdsByUser(userId);
    }

    @Transactional(readOnly = true)
    public Collection getOrThrowException(Long filmCollectionId, User user) {
        Collection collection = collectionRepository.findById(filmCollectionId)
                .orElseThrow(() -> new EntityNotFoundException("Film collection not found"));
        if (!collection.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Permission not found");
        }
        return collection;
    }
}
