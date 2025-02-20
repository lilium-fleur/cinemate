package com.fleur.cinemate.token.refresh;

import com.fleur.cinemate.__shared.jwt.JwtTokenUtil;
import com.fleur.cinemate.token.shared.TokenService;
import com.fleur.cinemate.token.shared.dto.CreateTokenDto;
import com.fleur.cinemate.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenService extends TokenService<RefreshToken> {

    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(
            RefreshTokenRepository tokenRepository,
            JwtTokenUtil jwtTokenUtil) {
        super(tokenRepository);
        this.jwtTokenUtil = jwtTokenUtil;
        this.refreshTokenRepository = tokenRepository;
    }

    @Override
    protected RefreshToken createTokenEntity(CreateTokenDto createTokenDto, Instant expiresAt) {
        return RefreshToken.builder()
                .token(createTokenDto.token())
                .expiresAt(expiresAt)
                .userSession(createTokenDto.userSession())
                .build();
    }

    @Override
    public boolean isTokenValid(String token, String fingerprint) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));

        return refreshToken.getUserSession().getFingerprint().equals(fingerprint) &&
                refreshToken.getExpiresAt().isAfter(Instant.now()) &&
                !refreshToken.getIsRevoked();
    }

    @Override
    protected long getExpirationTime() {
        return jwtTokenUtil.getRefreshTokenExpiration();
    }

    @Override
    protected String generateToken(User user) {
        return jwtTokenUtil.generateRefreshToken(user);
    }
}
