package com.fleur.cinemate.auth;

import com.fleur.cinemate.__shared.jwt.JwtTokenUtil;
import com.fleur.cinemate.auth.dto.AuthDto;
import com.fleur.cinemate.auth.dto.LoginDto;
import com.fleur.cinemate.auth.dto.RegisterDto;
import com.fleur.cinemate.token.access.AccessTokenService;
import com.fleur.cinemate.token.refresh.RefreshToken;
import com.fleur.cinemate.token.refresh.RefreshTokenService;
import com.fleur.cinemate.token.shared.TokenEntity;
import com.fleur.cinemate.token.shared.dto.CreateTokenDto;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.user.UserMapper;
import com.fleur.cinemate.user.UserService;
import com.fleur.cinemate.usersession.UserSession;
import com.fleur.cinemate.usersession.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final UserSessionService userSessionService;
    private final AccessTokenService accessTokenService;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public AuthDto register(RegisterDto registerDto, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.createUser(registerDto);
        saveFingerprintCookie(registerDto.fingerprint(), response);
        TokenEntity accessToken = createAuthenticationSession(user, registerDto.fingerprint(), request, response);

        return AuthDto.builder()
                 .userDto(userMapper.toDto(user))
                 .token(accessToken.getToken())
                 .build();
    }

    @Transactional
    public AuthDto login(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        if(!userService.matchPassword(loginDto.email(), loginDto.password())){
            throw new AccessDeniedException("Invalid email or password");
        }
        saveFingerprintCookie(loginDto.fingerprint(), response);
        User user = userService.findUserByEmail(loginDto.email());
        userSessionService.deactivateUserSessionCompletely(user, loginDto.fingerprint());
        TokenEntity accessToken = createAuthenticationSession(user, loginDto.fingerprint(), request, response);

        return AuthDto.builder()
                .userDto(userMapper.toDto(user))
                .token(accessToken.getToken())
                .build();
    }

    @Transactional
    public void logout(String refreshToken, String fingerprint, HttpServletResponse response) {
        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

        if(!refreshTokenService.isTokenValid(refreshToken, fingerprint)){
            userSessionService.deactivateUserSession(refreshTokenEntity.getUserSession());

            throw new AccessDeniedException("Invalid refresh token");
        }

        userSessionService.deactivateUserSession(refreshTokenEntity.getUserSession());

        ResponseCookie refreshCookie = ResponseCookie.from("__tar", "")
                .httpOnly(true)
                .path("/api/auth")
                .maxAge(0)
                .secure(false)
                .sameSite("Lax")
                .build();

        ResponseCookie fingerprintCookie = ResponseCookie.from("__paf", fingerprint)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .secure(false)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, fingerprintCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

    }


    @Transactional
    public AuthDto refresh(String refreshToken, String fingerprint) {
        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

        if (!refreshTokenService.isTokenValid(refreshToken, fingerprint)) {
            userSessionService.deactivateUserSession(refreshTokenEntity.getUserSession());

            throw new AccessDeniedException("Invalid refresh token");
        }

        accessTokenService.deactivateActiveTokens(refreshTokenEntity.getUserSession());

        CreateTokenDto createTokenDto = CreateTokenDto.builder()
                .userSession(refreshTokenEntity.getUserSession())
                .user(refreshTokenEntity.getUserSession().getUser())
                .build();

        TokenEntity accessToken = accessTokenService.createToken(createTokenDto);

        return AuthDto.builder()
                .userDto(userMapper.toDto(refreshTokenEntity.getUserSession().getUser()))
                .token(accessToken.getToken())
                .build();
    }


    private void saveFingerprintCookie(String fingerprint, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("__paf", fingerprint)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtTokenUtil.getRefreshTokenExpiration())
                .secure(false)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private TokenEntity createAuthenticationSession(
            User user, String fingerprint, HttpServletRequest request, HttpServletResponse response) {

        UserSession userSession = userSessionService.create(user, fingerprint, request);

        CreateTokenDto createTokenDto = CreateTokenDto.builder()
                .userSession(userSession)
                .user(user)
                .build();

        TokenEntity accessToken = accessTokenService.createToken(createTokenDto);
        TokenEntity refreshToken = refreshTokenService.createToken(createTokenDto);

        ResponseCookie refreshCookie = ResponseCookie.from("__tar", refreshToken.getToken())
                .httpOnly(true)
                .path("/api/auth")
                .maxAge(jwtTokenUtil.getRefreshTokenExpiration())
                .secure(false)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return accessToken;
    }
}
