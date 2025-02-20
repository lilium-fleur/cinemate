package com.fleur.cinemate.usersession;

import com.fleur.cinemate.token.access.AccessTokenService;
import com.fleur.cinemate.token.refresh.RefreshTokenService;
import com.fleur.cinemate.user.User;
import com.fleur.cinemate.usersession.dto.UserSessionDto;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserSessionMapper userSessionMapper;

    @Transactional
    public UserSession create(User user, String fingerprint, HttpServletRequest request) {
            UserSession userSession = UserSession.builder()
                    .ipAddress(getClientIpAddress(request))
                    .deviceInfo(getDeviceInfo(request))
                    .fingerprint(fingerprint)
                    .lastActivity(Instant.now())
                    .user(user)
                    .build();

            return userSessionRepository.save(userSession);

    }

    @Transactional(readOnly = true)
    public Page<UserSessionDto> findAllUserSessionByUser(User user, Pageable pageable) {
         return userSessionRepository.findAllByUser(user, pageable)
                 .map(userSessionMapper::toDto);
    }

    public void deactivateUserSessionCompletely(User user, String fingerprint){
        UserSession userSession = userSessionRepository.findActiveByFingerprintAndUser(fingerprint, user)
                .orElseThrow(() -> new EntityNotFoundException("User session not found"));
        userSessionRepository.deactivateUserSession(userSession);
        accessTokenService.deactivateActiveTokens(userSession);
        refreshTokenService.deactivateActiveTokens(userSession);
    }

    @Transactional
    public void deactivateUserSession(UserSession userSession) {
        userSession.setIsActive(false);

        accessTokenService.deactivateActiveTokens(userSession);
        refreshTokenService.deactivateActiveTokens(userSession);

        userSessionRepository.save(userSession);
    }

    public void deactivateUserSessionById(Long userSessionId) {
        UserSession userSession = userSessionRepository.findById(userSessionId)
                .orElseThrow(() -> new EntityNotFoundException("UserSession not found"));
        userSession.setIsActive(false);
        accessTokenService.deactivateActiveTokens(userSession);
        refreshTokenService.deactivateActiveTokens(userSession);
        userSessionRepository.save(userSession);
    }

    @Transactional
    public void deactivateAllOtherUserSessions(User user, HttpServletRequest request) {
        UserSession userSession = findUserSessionByUser(user, request);
        userSessionRepository.deactivateAllOtherUserSessions(userSession);
    }
    

    @Transactional(readOnly = true)
    public UserSession findUserSessionByUser(User user, HttpServletRequest req) {
        return userSessionRepository.findActiveByFingerprintAndUser(
                getFingerprint(req), user
                )
                .orElseThrow(() -> new EntityNotFoundException("UserSession not found"));

    }

    public String getDeviceInfo(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

        String deviceInfo = userAgent.getBrowser().getName();

        if(userAgent.getBrowserVersion() != null){
            deviceInfo += " " + userAgent.getBrowserVersion();
        }

        deviceInfo += " " + userAgent.getOperatingSystem().getName();
        deviceInfo += " " + userAgent.getOperatingSystem().getDeviceType();

        return deviceInfo;
    }

    public String getClientIpAddress(HttpServletRequest request) {
        String[] IP_HEADERS = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String header : IP_HEADERS) {
            String value = request.getHeader(header);

            if(value != null && !value.isEmpty() && !value.equalsIgnoreCase("unknown")) {

                String[] parts = value.split("\\s*,\\s*");
                return parts[0];
            }
        }
        return request.getRemoteAddr();
    }

    public String getFingerprint(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();


        if(cookies == null){
            return null;
        }

        Cookie cookie = Arrays.stream(cookies)
                .filter(cookie1 -> cookie1.getName().equals("__paf"))
                .findFirst()
                .orElse(null);

        return cookie != null ? cookie.getValue() : null;
    }

}
