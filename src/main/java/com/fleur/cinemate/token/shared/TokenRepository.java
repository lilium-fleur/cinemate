package com.fleur.cinemate.token.shared;

import com.fleur.cinemate.usersession.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository<T extends TokenEntity> extends JpaRepository<T, Long> {

    @Modifying
    @Query("UPDATE #{#entityName} t SET t.isRevoked = true " +
            "WHERE t.userSession = :userSession " +
            "AND t.isRevoked = false")
    void revokeAllActiveTokensByUserSession(UserSession userSession);

    Optional<T> findByToken(String token);
}
