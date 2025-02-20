package com.fleur.cinemate.usersession;

import com.fleur.cinemate.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {


    @Query("SELECT us FROM UserSession us " +
            "WHERE us.user = :user " +
            "AND us.fingerprint = :fingerprint " +
            "AND us.isActive = true")
    Optional<UserSession> findActiveByFingerprintAndUser(String fingerprint, User user);

    Page<UserSession> findAllByUser(User user, Pageable pageable);

    @Modifying
    @Query("UPDATE UserSession us SET us.isActive = false " +
            "WHERE us.user = :#{#userSession.user} " +
            "AND us.id != :#{#userSession.id} " +
            "AND us.isActive = true ")
    void deactivateAllOtherUserSessions(UserSession userSession);

    @Modifying
    @Query("UPDATE UserSession us SET us.isActive = false " +
            "WHERE us.user = :#{#userSession.user} " +
            "AND us.user = :#{#userSession.id} " +
            "AND us.isActive = true")
    void deactivateUserSession(UserSession userSession);

}
