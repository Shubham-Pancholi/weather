package com.spring.weather.dto;

import java.time.LocalDateTime;

public record WeatherResponse(
    String city,
    String country,
    Double lat,
    Double lon,
    Double temperature,
    Double feelsLike,
    Double tempMin,
    Double tempMax,
    Integer humidity,
    Integer pressure,
    Double windSpeed,
    Integer windDeg,
    String description,
    String icon,
    Integer visibility,
    LocalDateTime sunrise,
    LocalDateTime sunset,
    LocalDateTime timestamp
) implements java.io.Serializable {}