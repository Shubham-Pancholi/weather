package com.spring.weather.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ForecastResponse(
    String city,
    String country,
    List<ForecastItem> forecasts
) implements java.io.Serializable {
    public record ForecastItem(
        LocalDateTime dateTime,
        Double temperature,
        Double feelsLike,
        Integer humidity,
        String description,
        String icon,
        Double windSpeed,
        Double pop
    ) implements  java.io.Serializable {}
}