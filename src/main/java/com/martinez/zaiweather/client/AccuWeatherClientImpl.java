package com.martinez.zaiweather.client;

import com.martinez.zaiweather.constants.WeatherConstants;
import com.martinez.zaiweather.dto.WeatherData;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
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
import java.util.List;
import java.util.Map;

import java.net.URI;

@Order(1)
@Component
public class AccuWeatherClientImpl implements WeatherClient {

    @Value("${weather.apis.accuweather.key}")
    private String accuWeatherKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getProviderName() {
        return WeatherConstants.ACCUWEATHER;
    }

    @Override
    public WeatherData getWeatherData(String city) {
        // Prepare settings for the call
        String key = accuWeatherKey;

        // Call OpenWeather API
        return fetchFromAccuWeather(city, key);
    }

    public WeatherData fetchFromAccuWeather(String city, String key) {
        // Step 1: Get Location Key
        URI locationUri = UriComponentsBuilder
                .fromUriString("http://dataservice.accuweather.com/locations/v1/cities/search")
                .queryParam("apikey", key)
                .queryParam("q", city)
                .build()
                .toUri();

        ResponseEntity<List<Map<String, Object>>> locationResponse = restTemplate.exchange(
                locationUri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> locationList = locationResponse.getBody();
        if (locationList == null || locationList.isEmpty()) {
            throw new RuntimeException("City not found in AccuWeather: " + city);
        }

        String locationKey = (String) locationList.get(0).get("Key");

        // Step 2: Get Current Conditions
        URI weatherUri = UriComponentsBuilder
                .fromUriString("http://dataservice.accuweather.com/currentconditions/v1/" + locationKey)
                .queryParam("apikey", key)
                .build()
                .toUri();

        ResponseEntity<List<Map<String, Object>>> weatherResponse = restTemplate.exchange(
                weatherUri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> weatherList = weatherResponse.getBody();
        if (weatherList == null || weatherList.isEmpty()) {
            throw new RuntimeException("Weather data not available for city: " + city);
        }

        Map<String, Object> weatherInfo = weatherList.get(0);
        Map<String, Object> temperature = (Map<String, Object>) weatherInfo.get("Temperature");
        Map<String, Object> metric = (Map<String, Object>) temperature.get("Metric");

        double temp = ((Number) metric.get("Value")).doubleValue();
        double windSpeed = -1.0;
        if (weatherInfo.containsKey("Wind")) {
            Map<String, Object> wind = (Map<String, Object>) weatherInfo.get("Wind");
            Map<String, Object> windSpeedMap = (Map<String, Object>) wind.get("Speed");
            Map<String, Object> windMetric = (Map<String, Object>) windSpeedMap.get("Metric");
            windSpeed = ((Number) windMetric.get("Value")).doubleValue();
        }

        WeatherData weatherData = new WeatherData(getProviderName(), city);
        weatherData.setTemperature(temp);
        weatherData.setWindSpeed(windSpeed);

        return weatherData;
    }

}