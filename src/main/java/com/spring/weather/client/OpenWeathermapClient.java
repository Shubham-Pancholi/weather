package com.spring.weather.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.spring.weather.dto.OwmCurrentWeatherResponse;
import com.spring.weather.dto.OwmForecastResponse;
import com.spring.weather.exception.CityNotFoundException;
import com.spring.weather.exception.WeatherApiException;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class OpenWeathermapClient {

    private final RestClient restClient;

    public OwmCurrentWeatherResponse getCurrentByCity(String city) {
        return restClient.get()
                         .uri(uriBuilder -> uriBuilder.path("/data/2.5/weather")
                                                      .queryParam("q", city)
                                                      .queryParam("units", "metric")
                                                      .build()
                         )
                         .retrieve()
                         .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                            if (response.getStatusCode().value() == 404) {
                                throw new CityNotFoundException("City not found: " + city);
                            }
                            throw new WeatherApiException("Client error calling OpenWeatherMap: " + response.getStatusCode());
                         })
                         .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                            throw new WeatherApiException("Server error calling OpenWeatherMap: " + response.getStatusCode());
                         })
                         .body(OwmCurrentWeatherResponse.class);
    }

    public OwmCurrentWeatherResponse getCurrentByCoords(double lat, double lon) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/weather")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                    if (response.getStatusCode().value() == 404) {
                        throw new CityNotFoundException("Location not found for coords: " + lat + ", " + lon);
                    }
                    throw new WeatherApiException("Client error calling OpenWeatherMap: " + response.getStatusCode());
                })
                .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                    throw new WeatherApiException("Server error calling OpenWeatherMap: " + response.getStatusCode());
                })
                .body(OwmCurrentWeatherResponse.class);
    }

    public OwmForecastResponse getForecastByCity(String city) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/forecast")
                        .queryParam("q", city)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                    if (response.getStatusCode().value() == 404) {
                        throw new CityNotFoundException("City not found: " + city);
                    }
                    throw new WeatherApiException("Client error calling OpenWeatherMap: " + response.getStatusCode());
                })
                .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                    throw new WeatherApiException("Server error calling OpenWeatherMap: " + response.getStatusCode());
                })
                .body(OwmForecastResponse.class);
    }

    public OwmForecastResponse getForecastByCoords(double lat, double lon) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/forecast")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                    if (response.getStatusCode().value() == 404) {
                        throw new CityNotFoundException("Location not found for coords: " + lat + ", " + lon);
                    }
                    throw new WeatherApiException("Client error calling OpenWeatherMap: " + response.getStatusCode());
                })
                .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                    throw new WeatherApiException("Server error calling OpenWeatherMap: " + response.getStatusCode());
                })
                .body(OwmForecastResponse.class);
    }
}