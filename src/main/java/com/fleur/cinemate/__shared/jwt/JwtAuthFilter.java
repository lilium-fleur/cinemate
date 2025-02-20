package com.fleur.cinemate.__shared.jwt;

import com.fleur.cinemate.token.access.AccessTokenService;
import com.fleur.cinemate.user.CustomUserDetails;
import com.fleur.cinemate.usersession.UserSessionService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {


    private final UserSessionService userSessionService;
    private final AccessTokenService accessTokenService;
    private final CustomUserDetails customUserDetails;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {

            String authorization = request.getHeader("Authorization");

            if(StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
                throw new JwtException("Invalid JWT token");
            }

            String token = authorization.substring(7);
            String fingerprint = userSessionService.getFingerprint(request);
            String username = jwtTokenUtil.extractUsername(token);

            if(StringUtils.isBlank(username) && !accessTokenService.isTokenValid(token, fingerprint)) {
                throw new JwtException("Invalid JWT token");
            }

            UserDetails user = customUserDetails.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);

        } catch (JwtException | UsernameNotFoundException e) {
            log.error("Authentication error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
        } finally {
            if (!isPublicEndpoint(request)) {
                SecurityContextHolder.clearContext();
            }
        }


    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        return request.getRequestURI().contains("/api/auth/login") ||
                request.getRequestURI().contains("/api/auth/register") ||
                request.getRequestURI().contains("/api/auth/refresh");
    }
}
