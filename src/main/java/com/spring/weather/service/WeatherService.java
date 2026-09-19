package com.spring.weather.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.spring.weather.client.OpenWeathermapClient;
import com.spring.weather.dto.ForecastResponse;
import com.spring.weather.dto.WeatherResponse;
import com.spring.weather.entity.SavedAddress;
import com.spring.weather.mapper.WeatherMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class WeatherService {

    private final OpenWeathermapClient client;
    private final WeatherMapper mapper;

    @Cacheable (value = "current", key = "#city.toLowerCase()")
    public WeatherResponse getCurrentWeatherByCity(String city) {
        return mapper.toWeatherResponse(client.getCurrentByCity(city));
    }

    @Cacheable(value = "current", key = "T(String).format('%.2f,%.2f', #lat, #lon)")
    public WeatherResponse getCurrentWeatherByCoords(double lat, double lon) {
        return mapper.toWeatherResponse(client.getCurrentByCoords(lat, lon));
    }

    @Cacheable(value = "forecast", key = "#city.toLowerCase()")
    public ForecastResponse getForecastByCity(String city) {
        return mapper.toForecastResponse(client.getForecastByCity(city));
    }

    @Cacheable(value = "forecast", key = "T(String).format('%.2f,%.2f', #lat, #lon)")
    public ForecastResponse getForecastByCoords(double lat, double lon) {
        return mapper.toForecastResponse(client.getForecastByCoords(lat, lon));
    }

    public List<WeatherResponse> getCurrentWeatherForSaved(List<SavedAddress> addresses) {
        return  addresses.stream()
                         .map(address -> (address.getCity() != null ? getCurrentWeatherByCity(address.getCity()) : getCurrentWeatherByCoords(address.getLatitude(), address.getLongitude())))
                         .toList();
    }
}