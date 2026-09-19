package com.spring.weather.mapper;

import com.spring.weather.dto.ForecastResponse;
import com.spring.weather.dto.WeatherResponse;
import com.spring.weather.dto.OwmCurrentWeatherResponse;
import com.spring.weather.dto.OwmForecastResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeatherMapper {

    public WeatherResponse toWeatherResponse(OwmCurrentWeatherResponse owm) {
        if (owm == null) return null;

        String description = null;
        String icon = null;
        if (owm.weather() != null && !owm.weather().isEmpty()) {
            description = owm.weather().get(0).description();
            icon = owm.weather().get(0).icon();
        }

        return new WeatherResponse(
                owm.name(),
                owm.sys() != null ? owm.sys().country() : null,
                owm.coord() != null ? owm.coord().lat() : null,
                owm.coord() != null ? owm.coord().lon() : null,
                owm.main() != null ? owm.main().temp() : null,
                owm.main() != null ? owm.main().feelsLike() : null,
                owm.main() != null ? owm.main().tempMin() : null,
                owm.main() != null ? owm.main().tempMax() : null,
                owm.main() != null ? owm.main().humidity() : null,
                owm.main() != null ? owm.main().pressure() : null,
                owm.wind() != null ? owm.wind().speed() : null,
                owm.wind() != null ? owm.wind().deg() : null,
                description,
                icon,
                owm.visibility(),
                owm.sys() != null && owm.sys().sunrise() != null ? 
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(owm.sys().sunrise()), ZoneOffset.UTC) : null,
                owm.sys() != null && owm.sys().sunset() != null ? 
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(owm.sys().sunset()), ZoneOffset.UTC) : null,
                owm.dt() != null ? 
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(owm.dt()), ZoneOffset.UTC) : null
        );
    }

    public ForecastResponse toForecastResponse(OwmForecastResponse owm) {
        if (owm == null) return null;

        List<ForecastResponse.ForecastItem> items = owm.list() != null ? owm.list().stream()
                .map(item -> {
                    String description = null;
                    String icon = null;
                    if (item.weather() != null && !item.weather().isEmpty()) {
                        description = item.weather().get(0).description();
                        icon = item.weather().get(0).icon();
                    }

                    LocalDateTime dt = null;
                    if (item.dtTxt() != null) {
                        dt = LocalDateTime.parse(item.dtTxt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    return new ForecastResponse.ForecastItem(
                            dt,
                            item.main() != null ? item.main().temp() : null,
                            item.main() != null ? item.main().feelsLike() : null,
                            item.main() != null ? item.main().humidity() : null,
                            description,
                            icon,
                            item.wind() != null ? item.wind().speed() : null,
                            item.pop()
                    );
                })
                .collect(Collectors.toList()) : List.of();

        return new ForecastResponse(
                owm.city() != null ? owm.city().name() : null,
                owm.city() != null ? owm.city().country() : null,
                items
        );
    }
}
