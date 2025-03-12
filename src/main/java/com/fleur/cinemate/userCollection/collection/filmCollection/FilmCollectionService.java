package com.fleur.cinemate.userCollection.collection.filmCollection;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userCollection.collection.filmCollection.dto.CreateFilmCollectionDto;
import com.fleur.cinemate.userCollection.collection.filmCollection.dto.FilmCollectionDto;
import com.fleur.cinemate.userCollection.collection.filmCollection.dto.UpdateFilmCollectionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FilmCollectionService {

    private final FilmCollectionRepository filmCollectionRepository;
    private final FilmCollectionMapper filmCollectionMapper;

    @Transactional
    public FilmCollectionDto createFilmCollection(
            CreateFilmCollectionDto createFilmCollectionDto, User user) {

        FilmCollection filmCollection = FilmCollection.builder()
                .name(createFilmCollectionDto.name())
                .description(createFilmCollectionDto.description())
                .user(user)
                .isPublic(createFilmCollectionDto.isPublic())
                .build();

        return filmCollectionMapper.toDto(filmCollectionRepository.save(filmCollection));
    }

    @Transactional
    public FilmCollectionDto updateFilmCollection(
            UpdateFilmCollectionDto updateFilmCollectionDto, Long filmCollectionId, User user) {
        FilmCollection filmCollection = getOrThrowException(filmCollectionId, user);

        filmCollectionMapper.updateEntityFromDto(updateFilmCollectionDto, filmCollection);

        return filmCollectionMapper.toDto(filmCollectionRepository.save(filmCollection));
    }

    @Transactional
    public void deleteFilmCollection(Long filmCollectionId, User user) {
        FilmCollection filmCollection = getOrThrowException(filmCollectionId, user);
        filmCollectionRepository.delete(filmCollection);
    }

    @Transactional(readOnly = true)
    public FilmCollectionDto findFilmCollectionById(Long filmCollectionId, User currentUser) {
        FilmCollection filmCollection = filmCollectionRepository.findById(filmCollectionId)
                .orElseThrow(() -> new EntityNotFoundException("Film collection not found"));
        if(!filmCollection.getIsPublic() && !filmCollection.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission not found");
        }
        return filmCollectionMapper.toDto(filmCollection);
    }

    @Transactional(readOnly = true)
    public FilmCollection findFilmCollectionEntityById(Long filmCollectionId){
        return filmCollectionRepository.findById(filmCollectionId)
                .orElseThrow(() -> new EntityNotFoundException("Film collection not found"));
    }

    @Transactional(readOnly = true)
    public Page<FilmCollectionDto> findFilmCollectionsByUser(User currentUser, Long userId, Pageable pageable) {
        if(currentUser.getId().equals(userId)) {
            return findFilmCollectionsForCurrentUser(currentUser, pageable);
        }
        return findPublicFilmCollectionsByUser(userId, pageable);
    }

    @Transactional(readOnly = true)
    public FilmCollection getOrThrowException(Long filmCollectionId, User user) {
        FilmCollection filmCollection = filmCollectionRepository.findById(filmCollectionId)
                .orElseThrow(() -> new EntityNotFoundException("Film collection not found"));
        if(!filmCollection.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Permission not found");
        }
        return filmCollection;
    }

    private Page<FilmCollectionDto> findAllPublicFilmCollections(Pageable pageable) {
        return filmCollectionRepository.findAllPublic(pageable)
                .map(filmCollectionMapper::toDto);
    }

    private Page<FilmCollectionDto> findFilmCollectionsForCurrentUser(User currentUser, Pageable pageable) {
        return filmCollectionRepository.findAllByUser(currentUser, pageable)
                .map(filmCollectionMapper::toDto);
    }

    private Page<FilmCollectionDto> findPublicFilmCollectionsByUser(Long userId, Pageable pageable) {
        return filmCollectionRepository.findAllPublicByUserId(userId, pageable)
                .map(filmCollectionMapper::toDto);
    }
}
