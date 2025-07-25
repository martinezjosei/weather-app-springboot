package com.martinez.zaiweather.service;

import com.martinez.zaiweather.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Service

public class WeatherClient {

    @Value("${weather.apis.weatherstack.key}")
    private String weatherstackKey;

    @Value("${weather.apis.openweather.key}")
    private String openWeatherKey;


    private final RestTemplate restTemplate = new RestTemplate();


    @SuppressWarnings("unchecked")
    public WeatherResponse fetchFromWeatherStack(String city) {
        String key = weatherstackKey;

        URI uri = UriComponentsBuilder
                .fromUriString("http://api.weatherstack.com/current")
                .queryParam("query", city)
                .queryParam("access_key", key)
                .queryParam("units", "m")
                .build()
                .toUri();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> body = response.getBody();


        assert body != null;
        Map<String, Object> current = (Map<String, Object>) body.get("current");
        double temp = ((Number) current.get("temperature")).doubleValue();
        double windSpeed = ((Number) current.get("wind_speed")).doubleValue();
        return new WeatherResponse(temp, windSpeed);
    }

    @SuppressWarnings("unchecked")
    public WeatherResponse fetchFromOpenWeather(String city) {
        String key = openWeatherKey;

        URI uri = UriComponentsBuilder
                .fromUriString("http://api.openweathermap.org/data/2.5/weather")
                .queryParam("q", city)
                .queryParam("appid", key)
                .queryParam("units", "metric")
                .build()
                .toUri();


        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> body = response.getBody();

        assert body != null;
        Map<String, Object> main = (Map<String, Object>) body.get("main");
        double temp = ((Number) main.get("temp")).doubleValue();

        Map<String, Object> wind = (Map<String, Object>) body.get("wind");
        double windSpeed = ((Number) wind.get("speed")).doubleValue();

        return new WeatherResponse(temp, windSpeed);
    }
}

