package com.spring.weather.exception;

public class WeatherApiException extends RuntimeException {
    public WeatherApiException(String message) {
        super(message);
    }
}