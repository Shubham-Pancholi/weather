package com.spring.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OwmCurrentWeatherResponse(
        String name,
        Coord coord,
        Main main,
        List<Weather> weather,
        Wind wind,
        Integer visibility,
        Sys sys,
        Long dt,
        Integer timezone
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Coord(Double lat, Double lon) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Main(
            Double temp,
            @JsonProperty("feels_like") Double feelsLike,
            @JsonProperty("temp_min") Double tempMin,
            @JsonProperty("temp_max") Double tempMax,
            Integer humidity,
            Integer pressure
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Weather(
            Integer id,
            String main,
            String description,
            String icon
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Wind(Double speed, Integer deg) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Sys(
            String country,
            Long sunrise,
            Long sunset
    ) {}
}