package com.fleur.cinemate.userCollection.userList;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserListRepository extends JpaRepository<UserList, Long> {

    Optional<UserList> findByUserAndFilmAndType(User user, Film film, UserListType type);

    Page<UserList> findByUserAndType(User user, UserListType type, Pageable pageable);
}
