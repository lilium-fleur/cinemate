package com.fleur.cinemate.token.access;

import com.fleur.cinemate.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends TokenRepository<AccessToken> {
}
