package com.fleur.cinemate.userList;

import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserListRepository extends JpaRepository<UserList, Long> {

    Optional<UserList> findByUserAndFilmAndType(User user, Film film, UserListType type);

    Page<UserList> findByUserAndType(User user, UserListType type, Pageable pageable);

    @Query("SELECT DISTINCT ul.film.id FROM UserList ul " +
            "WHERE ul.user.id = :userId")
    Set<Long> findDistinctByUser(Long userId);
}
