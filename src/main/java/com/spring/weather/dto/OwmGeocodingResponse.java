package com.spring.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OwmGeocodingResponse(
        String name,
        Double lat,
        Double lon,
        String country,
        String state
) {}