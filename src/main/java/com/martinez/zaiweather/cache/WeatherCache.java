package com.martinez.zaiweather.cache;

import com.martinez.zaiweather.dto.WeatherResponse;

import java.time.Instant;

public class WeatherCache {
    private static WeatherResponse cachedData;
    private static Instant lastUpdated;

    public static WeatherResponse getCache() {
        if (lastUpdated != null && Instant.now().minusSeconds(3).isBefore(lastUpdated)) {
            return cachedData;
        }
        return null;
    }

    public static void setCache(WeatherResponse response) {
        cachedData = response;
        lastUpdated = Instant.now();
    }
}