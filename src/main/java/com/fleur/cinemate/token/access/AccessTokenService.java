package com.fleur.cinemate.token.access;

import com.fleur.cinemate.__shared.jwt.JwtTokenUtil;
import com.fleur.cinemate.token.shared.TokenService;
import com.fleur.cinemate.token.shared.dto.CreateTokenDto;
import com.fleur.cinemate.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AccessTokenService extends TokenService<AccessToken> {
    private final JwtTokenUtil jwtTokenUtil;
    private final AccessTokenRepository accessTokenRepository;

    public AccessTokenService(
            AccessTokenRepository tokenRepository,
            JwtTokenUtil jwtTokenUtil) {
        super(tokenRepository);
        this.jwtTokenUtil = jwtTokenUtil;
        this.accessTokenRepository = tokenRepository;
    }

    @Override
    protected AccessToken createTokenEntity(CreateTokenDto createTokenDto, Instant expiresAt) {
        return AccessToken.builder()
                .token(createTokenDto.token())
                .expiresAt(expiresAt)
                .userSession(createTokenDto.userSession())
                .build();
    }

    @Override
    protected long getExpirationTime() {
        return jwtTokenUtil.getAccessTokenExpiration();
    }

    @Override
    protected String generateToken(User user) {
        return jwtTokenUtil.generateAccessToken(user);
    }

    @Override
    public boolean isTokenValid(String token, String fingerprint) {
        AccessToken accessToken = accessTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));

        return accessToken.getUserSession().getFingerprint().equals(fingerprint) &&
                accessToken.getExpiresAt().isAfter(Instant.now()) &&
                !accessToken.getIsRevoked();

    }

}
