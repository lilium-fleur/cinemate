package com.fleur.cinemate.token.shared;


import com.fleur.cinemate.token.shared.dto.CreateTokenDto;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.usersession.UserSession;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Service
public abstract class TokenService<T extends TokenEntity> {
        private final TokenRepository<T> tokenRepository;

    public TokenService(TokenRepository<T> tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public TokenEntity createToken(CreateTokenDto createTokenDto) {
        String jwt = generateToken(createTokenDto.user());

        Instant expiresAt = Instant.now().plusMillis(getExpirationTime() * 60 * 1000);

        T token = createTokenEntity(
            CreateTokenDto.builder()
                    .user(createTokenDto.user())
                    .token(jwt)
                    .userSession(createTokenDto.userSession())
                    .build(),
                expiresAt
        );

        return tokenRepository.save(token);
    }

    @Transactional
    public void deactivateActiveTokens(UserSession userSession){
        tokenRepository.revokeAllActiveTokensByUserSession(userSession);
    }

    @Transactional(readOnly = true)
    public T findByToken(String token){
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));
    }

    public abstract boolean isTokenValid(String token, String fingerprint);
    protected abstract long getExpirationTime();
    protected abstract T createTokenEntity(CreateTokenDto createTokenDto, Instant expiresAt);
    protected abstract String generateToken(User user);

}
