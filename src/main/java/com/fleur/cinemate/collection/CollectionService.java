package com.fleur.cinemate.collection;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.collection.dto.CreateCollectionDto;
import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.collection.dto.UpdateCollectionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionMapper collectionMapper;

    @Transactional
    public CollectionDto createCollection(
            CreateCollectionDto createCollectionDto, User user) {

        Collection collection = Collection.builder()
                .name(createCollectionDto.name())
                .description(createCollectionDto.description())
                .user(user)
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
    public void deleteCollection(Long filmCollectionId, User user) {
        Collection collection = getOrThrowException(filmCollectionId, user);
        collectionRepository.delete(collection);
    }

    @Transactional(readOnly = true)
    public CollectionDto findCollectionById(Long filmCollectionId, User currentUser) {
        Collection collection = collectionRepository.findById(filmCollectionId)
                .orElseThrow(() -> new EntityNotFoundException("Film collection not found"));
        if(!collection.getIsPublic() && !collection.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission not found");
        }
        return collectionMapper.toDto(collection);
    }

    @Transactional(readOnly = true)
    public Page<CollectionDto> findCollectionsByUser(User currentUser, Long userId, Pageable pageable) {
        if(currentUser.getId().equals(userId)) {
            return findCollectionsForCurrentUser(currentUser, pageable);
        }
        return findPublicCollectionsByUser(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Collection getOrThrowException(Long filmCollectionId, User user) {
        Collection collection = collectionRepository.findById(filmCollectionId)
                .orElseThrow(() -> new EntityNotFoundException("Film collection not found"));
        if(!collection.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Permission not found");
        }
        return collection;
    }

    private Page<CollectionDto> findCollectionsForCurrentUser(User currentUser, Pageable pageable) {
        return collectionRepository.findAllByUser(currentUser, pageable)
                .map(collectionMapper::toDto);
    }

    private Page<CollectionDto> findPublicCollectionsByUser(Long userId, Pageable pageable) {
        return collectionRepository.findAllPublicByUserId(userId, pageable)
                .map(collectionMapper::toDto);
    }
}
