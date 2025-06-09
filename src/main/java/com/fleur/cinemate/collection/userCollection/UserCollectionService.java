package com.fleur.cinemate.collection.userCollection;

import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.collection.CollectionMapper;
import com.fleur.cinemate.collection.CollectionService;
import com.fleur.cinemate.collection.dto.CollectionDto;
import com.fleur.cinemate.collection.userCollection.dto.CreateUserCollectionDto;
import com.fleur.cinemate.collection.userCollection.dto.UserCollectionDto;
import com.fleur.cinemate.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserCollectionService {
    private final UserCollectionRepository userCollectionRepository;
    private final CollectionService collectionService;
    private final UserCollectionMapper userCollectionMapper;
    private final CollectionMapper collectionMapper;

    @Transactional
    public UserCollectionDto addCollection(CreateUserCollectionDto createUserCollectionDto, User user) {
        Collection collection = collectionService
                .findPublicCollectionById(createUserCollectionDto.collectionId(), user.getId());

        UserCollection userCollection = UserCollection.builder()
                .user(user)
                .collection(collection)
                .build();
        return userCollectionMapper.toDto(userCollectionRepository.save(userCollection));
    }

    @Transactional
    public void removeCollection(Long collectionId, Long userId) {
        UserCollection userCollection = userCollectionRepository.findByUserIdAndCollectionId(userId, collectionId)
                .orElseThrow(() -> new EntityNotFoundException("User didn't add this collection"));
        userCollectionRepository.delete(userCollection);
    }

    @Transactional(readOnly = true)
    public Page<CollectionDto> getAllCollectionsByUser(Long userId, User currentUser, Pageable pageable) {
        if (currentUser.getId().equals(userId)) {
            return userCollectionRepository.findAllByUserId(userId, pageable)
                    .map(UserCollection::getCollection)
                    .map(collectionMapper::toDto);
        }
        return userCollectionRepository.findPublicCollectionsByUserId(userId, pageable)
                .map(UserCollection::getCollection)
                .map(collectionMapper::toDto);
    }

}
