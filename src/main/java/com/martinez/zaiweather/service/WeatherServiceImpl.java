package com.martinez.zaiweather.service;

import com.martinez.zaiweather.cache.WeatherCache;
import com.martinez.zaiweather.client.WeatherClient;
import com.martinez.zaiweather.domain.Weather;
import com.martinez.zaiweather.dto.WeatherData;
import com.martinez.zaiweather.dto.WeatherResponse;
import com.martinez.zaiweather.util.WeatherMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    List<WeatherClient> clients;

    public WeatherServiceImpl(List<WeatherClient> clients) {
        this.clients = clients;
    }

    public WeatherResponse getWeatherForCity(String city) {
        // Try to return a cached result if available
        WeatherData cachedData = WeatherCache.get(city);
        if (cachedData != null) {
            return processWeatherObject(toWeatherObject(cachedData));
        }

        return doGetWeather(city);
    }


    public WeatherResponse doGetWeather(String city){
        for (WeatherClient weatherClient : clients) {
            try {
                WeatherData weatherData = weatherClient.getWeatherData(city);
                if (weatherData != null) {
                    // Save to cache
                    WeatherCache.put(city, weatherData);

                    // Get the domain object - Weather
                    Weather weather = toWeatherObject(weatherData);

                    // Perform business logic on the domain object and return for presentation
                    return  processWeatherObject(weather);
                }
            } catch (Exception e) {
                // Log and continue trying next client
                System.err.println("Client:" + weatherClient.getProviderName() + " failed for city: " + city + ", error: " + e.getMessage());
            }
        }

        // All clients failed
        throw new RuntimeException("Unable to fetch weather data for city: " + city);
    }


    public WeatherResponse processWeatherObject(Weather weather){
        // Here we process the domain object - Weather and return the presentation object
        System.out.println("Processing data for " + weather.getProvider());

        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setTemperatureCelsius(weather.getTemperature());
        weatherResponse.setWindSpeed(weather.getWindSpeed());

        return weatherResponse;
    }


    // Now this domain class can be used in the service layer
    public Weather toWeatherObject(WeatherData weatherData){
        return WeatherMapper.toWeather(weatherData);
    }
}
