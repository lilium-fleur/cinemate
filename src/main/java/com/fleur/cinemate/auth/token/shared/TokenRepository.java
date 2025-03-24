package com.fleur.cinemate.auth.token.shared;

import com.fleur.cinemate.user.User;
import com.fleur.cinemate.user.session.UserSession;
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

    @Modifying
    @Query("UPDATE #{#entityName} t SET t.isRevoked = true " +
            "WHERE t.userSession.user = :user " +
            "AND t.userSession.fingerprint = :fingerprint " +
            "AND t.isRevoked = false")
    void revokeAllActiveTokensByFingerprintAndUser(String fingerprint, User user);

    Optional<T> findByToken(String token);
}
