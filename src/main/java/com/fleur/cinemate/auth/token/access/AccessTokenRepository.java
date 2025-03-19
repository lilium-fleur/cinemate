package com.fleur.cinemate.auth.token.access;

import com.fleur.cinemate.auth.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends TokenRepository<AccessToken> {
}
