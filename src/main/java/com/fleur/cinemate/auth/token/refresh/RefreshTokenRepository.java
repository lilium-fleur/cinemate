package com.fleur.cinemate.auth.token.refresh;

import com.fleur.cinemate.auth.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends TokenRepository<RefreshToken> {
}
