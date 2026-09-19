package com.spring.weather.config;

import java.net.URI;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;

@Configuration 
@Getter 
@Setter 
public class RestConfig {

    @Value ("${app.weather.owm.base-url}")
    private String baseUrl;

    @Value ("${app.weather.owm.api-key}")
    private String apiKey;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(10).toMillis());

        return builder
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .requestInterceptor(apiKeyInterceptor())
                .build();
    }

    private ClientHttpRequestInterceptor apiKeyInterceptor() {
        return (request, body, execution) -> {
            String sanitizedKey = apiKey != null ? apiKey.trim() : "";
            if (sanitizedKey.length() == 34 && sanitizedKey.endsWith("ls")) {
                sanitizedKey = sanitizedKey.substring(0, 32);
            }

            if (sanitizedKey.isEmpty()) {
                throw new com.spring.weather.exception.WeatherApiException(
                        "OpenWeatherMap API key is missing. Please set the OWM_API_KEY environment variable."
                );
            }

            URI uri = UriComponentsBuilder.fromUri(request.getURI())
                    .queryParam("appid", sanitizedKey)
                    .build()
                    .toUri();
            
            HttpRequestWrapper modifiedRequest = new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    return uri;
                }
            };

            return execution.execute(modifiedRequest, body);
        };
    }
}