package com.martinez.zaiweather.service;

import com.martinez.zaiweather.cache.WeatherCache;
import com.martinez.zaiweather.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherClient weatherClient;

    public WeatherResponse getWeatherForCity(String city) {
        // Try to return a fresh cached result if available
        WeatherResponse cached = WeatherCache.getCache();
        if (cached != null) {
            return cached;
        }

        try {
            // Try primary provider: WeatherStack
            WeatherResponse response = weatherClient.fetchFromWeatherStack(city);
            WeatherCache.setCache(response);
            return response;
        } catch (Exception exception1) {
            try {
                // If WeatherStack fails, try OpenWeatherMap
                WeatherResponse response = weatherClient.fetchFromOpenWeather(city);
                WeatherCache.setCache(response);
                return response;
            } catch (Exception exception2) {
                WeatherResponse stale = WeatherCache.getCache();
                if (stale != null) {
                    return stale;
                }
                // No hope!
                throw new RuntimeException("No weather provider is available at this time");
            }
        }
    }
}
