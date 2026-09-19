package com.spring.weather.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.spring.weather.config.JwtProperties;

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

    public String generateToken(Long userId, String email) {
        Map<String, Object> extraClaims = new HashMap<>();
    }
}