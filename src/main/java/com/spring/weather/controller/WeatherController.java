package com.spring.weather.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.weather.dto.ForecastResponse;
import com.spring.weather.dto.WeatherResponse;
import com.spring.weather.service.WeatherService;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<WeatherResponse> getCurrentWeather(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon) {
        
        if (city != null && !city.isBlank()) {
            return ResponseEntity.ok(weatherService.getCurrentWeatherByCity(city));
        } else if (lat != null && lon != null) {
            return ResponseEntity.ok(weatherService.getCurrentWeatherByCoords(lat, lon));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/forecast")
    public ResponseEntity<ForecastResponse> getForecast(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon) {
        
        if (city != null && !city.isBlank()) {
            return ResponseEntity.ok(weatherService.getForecastByCity(city));
        } else if (lat != null && lon != null) {
            return ResponseEntity.ok(weatherService.getForecastByCoords(lat, lon));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}