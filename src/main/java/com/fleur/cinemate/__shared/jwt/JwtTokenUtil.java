package com.fleur.cinemate.__shared.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Data
@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>(), accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>(), refreshTokenExpiration);
    }

    public String generateToken(UserDetails userDetails,
                                HashMap<String, Object> claims,
                                Long expiration) {
        Date createdAt = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + expiration * 60 * 1000);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(createdAt)
                .expiration(expirationDate)
                .signWith(getSecretKey())
                .compact();

    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
