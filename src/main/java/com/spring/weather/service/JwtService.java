package com.spring.weather.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.spring.weather.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {
    
    private final String secretKey;
    private final Long jwtExpiration;
    private final CacheManager cacheManager;

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties aJwtProperties, CacheManager aCacheManager) {
        jwtProperties = aJwtProperties;
        cacheManager = aCacheManager;
        secretKey = jwtProperties.getSecret();
        jwtExpiration = jwtProperties.getExpiration();
    }

    private SecretKey getSignInKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String generateToken(String email) {
        return Jwts.builder()
                   .subject(email)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                   .signWith(getSignInKey())
                   .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                   .verifyWith(getSignInKey())
                   .build().parseSignedClaims(token)
                   .getPayload();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String userEmail) {
        try {
            Claims claims = extractClaims(token);
            boolean emailMatches = userEmail.equals(claims.getSubject());
            boolean notExpired = claims.getExpiration().after(new Date());

            Cache cache = cacheManager.getCache("invalid-tokens");
            boolean isNotInvalidated = (cache == null || cache.get(token) == null);

            return emailMatches && notExpired && isNotInvalidated;
        }   catch (Exception e) {
            return false;
        }
    }
}