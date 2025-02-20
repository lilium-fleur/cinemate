package com.fleur.cinemate.token.refresh;

import com.fleur.cinemate.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends TokenRepository<RefreshToken> {
}
