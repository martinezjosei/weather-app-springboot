package com.martinez.zaiweather.client;

import com.martinez.zaiweather.constants.WeatherConstants;
import com.martinez.zaiweather.dto.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Order(3)
@Component
public class OpenWeatherClientImpl implements WeatherClient {

    @Value("${weather.apis.openweather.key}")
    private String openWeatherKey;

    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public String getProviderName() {
        return WeatherConstants.OPENWEATHER;
    }

    @Override
    public WeatherData getWeatherData(String city) {
        // Prepare settings for the call
        String key = openWeatherKey;

        // Call OpenWeather API
        return fetchFromWeatherStack(city, key);
    }

    public WeatherData fetchFromWeatherStack(String city, String key) {

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

        WeatherData weatherData = new WeatherData(getProviderName(), city);
        weatherData.setTemperature(temp);
        weatherData.setWindSpeed(windSpeed);

        return weatherData;
    }

}
