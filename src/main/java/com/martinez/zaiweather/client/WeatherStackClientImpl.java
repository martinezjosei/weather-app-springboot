package com.martinez.zaiweather.client;

import com.martinez.zaiweather.constants.WeatherConstants;
import com.martinez.zaiweather.dto.WeatherData;
import com.martinez.zaiweather.dto.WeatherResponse;
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

@Order(2)
@Component
public class WeatherStackClientImpl implements WeatherClient {

    @Value("${weather.apis.weatherstack.key}")
    private String weatherstackKey;

    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public String getProviderName() {
        return WeatherConstants.WEATHERSTACK;
    }

    @Override
    public WeatherData getWeatherData(String city) {
        // Prepare settings for the call
        String key = weatherstackKey;

        // Call WeatherStack API
        return fetchFromWeatherStack(city, key);
    }


    public WeatherData fetchFromWeatherStack(String city, String key) {
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

        WeatherData weatherData = new WeatherData(getProviderName(), city);
        weatherData.setTemperature(temp);
        weatherData.setWindSpeed(windSpeed);

        return weatherData;
    }

}