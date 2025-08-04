package com.martinez.zaiweather.config;

import com.martinez.zaiweather.cache.WeatherCache;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeatherCacheConfig {

    @Value("${weather.cache.ttl:600000}") // default to 10 mins if not set
    private long ttl;

    @PostConstruct
    public void init() {
        WeatherCache.setTtlMillis(ttl);
    }
}
