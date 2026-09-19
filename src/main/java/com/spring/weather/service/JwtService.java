package com.spring.weather.service;

import java.util.Date;

import javax.crypto.SecretKey;

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

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties aJwtProperties) {
        jwtProperties = aJwtProperties;
        secretKey = jwtProperties.getSecret();
        jwtExpiration = jwtProperties.getExpiration();
    }

    private SecretKey getSignInKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String generateToken(String email, Integer version) {
        return Jwts.builder()
                   .claim("version", version)
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

    public Integer extractPasswordVersion(String token) {
        return extractClaims(token).get("version", Integer.class);
    }

    public boolean isTokenValid(String token, String userEmail) {
        Claims claims = extractClaims(token);
        return claims.getSubject().equals(userEmail) && claims.getExpiration().after(new Date());
    }
}