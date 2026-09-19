package com.spring.weather.dto;

public record AuthResponse(
    String token,
    String type,
    String email
) {
    public static AuthResponse of(String token, String email) {
        return new AuthResponse(token, "Bearer", email);
    }
}