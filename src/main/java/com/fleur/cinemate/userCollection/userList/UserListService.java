package com.fleur.cinemate.userCollection.userList;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.core.film.FilmService;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.userCollection.userList.dto.CreateUserListDto;
import com.fleur.cinemate.userCollection.userList.dto.UserListDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserListService {
    private final UserListRepository userListRepository;
    private final FilmService filmService;
    private final UserListMapper userListMapper;

    @Transactional
    public UserListDto addFilmToList(CreateUserListDto createUserListDto, User user){
        try {
            Film film = filmService.findFilmEntityById(createUserListDto.filmId());
            UserListType userListType = UserListType.valueOf(createUserListDto.type().toUpperCase());
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

            return userListMapper.toDto(userListRepository.save(userList));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(String.format("Invalid list type: %s", e.getMessage()));
        }

    }

    @Transactional
    public void removeFilmFromList(Long userListId){
        UserList userList = userListRepository.findById(userListId)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Film was not added to this list"));
        userListRepository.delete(userList);
    }

    @Transactional(readOnly = true)
    public Page<UserListDto> getFilmsByTypeList(User user, String listType, Pageable pageable){
        try {
            UserListType userListType = UserListType.valueOf(listType.toUpperCase());
            return userListRepository.findByUserAndType(user, userListType, pageable)
                    .map(userListMapper::toDto);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid list type");
        }
    }


}
