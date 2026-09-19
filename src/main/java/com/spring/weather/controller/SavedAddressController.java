package com.spring.weather.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.weather.dto.WeatherResponse;
import com.spring.weather.service.SavedAddressService;
import com.spring.weather.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/save")
@RequiredArgsConstructor 
public class SavedAddressController {

    private final SavedAddressService savedAddressService;
    private final WeatherService weatherService;

    @PostMapping 
    public ResponseEntity<WeatherResponse> saveAddress(
        @RequestParam (required = false) String city,
        @RequestParam (required = false) Double latitude,
        @RequestParam (required = false) Double longitude,
        @AuthenticationPrincipal String userEmail
    ) {
        if (city != null && !city.isBlank()) {
            savedAddressService.saveCity(city, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(weatherService.getCurrentWeatherByCity(city));            
        }   else if (latitude != null && longitude != null) {
            savedAddressService.saveCoords(latitude, longitude, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED)
                                  .body(weatherService.getCurrentWeatherByCoords(latitude, longitude));
        }
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping ("/current")
    public ResponseEntity<List<WeatherResponse>> getCurrentSaved(
        @AuthenticationPrincipal String userEmail
    ) {
        List<WeatherResponse> weatherResponses = weatherService.getCurrentWeatherForSaved(savedAddressService.getSavedAddresses(userEmail));

        return ResponseEntity.ok(weatherResponses);
    }
}