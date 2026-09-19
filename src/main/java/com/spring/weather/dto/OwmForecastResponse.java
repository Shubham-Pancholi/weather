package com.spring.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OwmForecastResponse(
        City city,
        List<ForecastItem> list
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record City(
            String name,
            String country,
            OwmCurrentWeatherResponse.Coord coord
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ForecastItem(
            OwmCurrentWeatherResponse.Main main,
            List<OwmCurrentWeatherResponse.Weather> weather,
            OwmCurrentWeatherResponse.Wind wind,
            Integer visibility,
            Double pop,
            @JsonProperty("dt_txt") String dtTxt
    ) {}
}