package com.spring.weather;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.spring.weather.dto.AuthRequest;
import com.spring.weather.dto.AuthResponse;
import com.spring.weather.dto.ErrorResponse;
import com.spring.weather.dto.ForecastResponse;
import com.spring.weather.dto.OwmCurrentWeatherResponse;
import com.spring.weather.dto.OwmForecastResponse;
import com.spring.weather.dto.OwmGeocodingResponse;
import com.spring.weather.dto.WeatherResponse;

@SpringBootApplication
@EnableCaching 
@RegisterReflectionForBinding({
    OwmCurrentWeatherResponse.class,
    OwmCurrentWeatherResponse.Coord.class,
    OwmCurrentWeatherResponse.Main.class,
    OwmCurrentWeatherResponse.Weather.class,
    OwmCurrentWeatherResponse.Wind.class,
    OwmCurrentWeatherResponse.Sys.class,
    OwmForecastResponse.class,
    OwmForecastResponse.City.class,
    OwmForecastResponse.ForecastItem.class,
    OwmGeocodingResponse.class,
    WeatherResponse.class,
    ForecastResponse.class,
    ForecastResponse.ForecastItem.class,
    AuthRequest.class,
    AuthResponse.class,
    ErrorResponse.class
})
public class WeatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherApplication.class, args);
	}
}
