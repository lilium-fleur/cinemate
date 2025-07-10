package com.fleur.cinemate.userList;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmService;
import com.fleur.cinemate.event.UserProfileUpdatedEvent;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userList.dto.CreateUserListDto;
import com.fleur.cinemate.userList.dto.UserListDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserListService {
    private final UserListRepository userListRepository;
    private final FilmService filmService;
    private final UserListMapper userListMapper;
    private final ApplicationEventPublisher eventPublisher;

    @CacheEvict(cacheNames = {"userProfile"}, key = "#user")
    @Transactional
    public UserListDto addFilmToList(CreateUserListDto createUserListDto, User user) {
        Film film = filmService.findFilmEntityById(createUserListDto.filmId());
        UserListType userListType = UserListType.fromString(createUserListDto.type());
        userListRepository.findByUserAndFilmAndType(user, film, userListType)
                .ifPresent(userList -> {
                    throw new BadRequestException
                            (String.format("This Film already added to list: %s", userListType));
                });

        UserList userList = UserList.builder()
                .user(user)
                .film(film)
                .type(userListType)
                .build();

        eventPublisher.publishEvent(new UserProfileUpdatedEvent(this, user));

        return userListMapper.toDto(userListRepository.save(userList));
    }

    @CacheEvict(cacheNames = {"userProfile"}, key = "#user")
    @Transactional
    public void removeFilmFromList(Long userListId, User user) {
        UserList userList = userListRepository.findById(userListId)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Film was not added to this list"));
        eventPublisher.publishEvent(new UserProfileUpdatedEvent(this, user));
        userListRepository.delete(userList);
    }

    @Transactional(readOnly = true)
    public Page<UserListDto> findItemsByTypeList(User user, String listType, Pageable pageable) {
        UserListType userListType = UserListType.fromString(listType);
        return userListRepository.findByUserAndType(user, userListType, pageable)
                .map(userListMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<Film> findFilmsByTypeList(User user, UserListType type, Pageable pageable) {
        return userListRepository.findByUserAndType(user, type, pageable)
                .map(UserList::getFilm);
    }

    @Transactional(readOnly = true)
    public Set<Long> findDistinctFilmIdByUser(Long userId) {
        return userListRepository.findDistinctByUser(userId);
    }


}
