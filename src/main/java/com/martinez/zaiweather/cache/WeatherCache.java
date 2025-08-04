package com.martinez.zaiweather.cache;

import com.martinez.zaiweather.dto.WeatherData;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;


public class WeatherCache {

    // TTL in milliseconds (1 minutes)
    private static long ttlMillis = 1 * 60 * 1000;

    // Max jitter percentage (e.g., 10%)
    private static final double JITTER_PERCENTAGE = 0.1;

    private static final ConcurrentHashMap<String, CacheEntry> concurrentHashMapCache = new ConcurrentHashMap<>();

    private WeatherCache() {
        // prevents instantiation
    }

    public static void setTtlMillis(long ttl) {
        ttlMillis = ttl;
    }

    public static WeatherData get(String city) {
        CacheEntry cacheEntry = concurrentHashMapCache.get(city.toLowerCase());
        if (cacheEntry != null) {
            if ( System.currentTimeMillis() < cacheEntry.expiryTime ) {
                return cacheEntry.response;
            } else {
                // Expired
                concurrentHashMapCache.remove(city.toLowerCase());
            }
        }
        return null;
    }

    public static void put(String city, WeatherData response) {
        long jitter = getRandomJitter();
        long expiryTimeMillis = System.currentTimeMillis() + ttlMillis + jitter;
        System.out.println("Jitter:" + jitter + ", expiryTime(sec):" + ((expiryTimeMillis / 1000) * 60) );
        concurrentHashMapCache.put(city.toLowerCase(), new CacheEntry(response, expiryTimeMillis));
    }

    // Optional: clear method for testing or manual invalidation
    public static void clearAll() {
        concurrentHashMapCache.clear();
    }

    private static long getRandomJitter() {
        long maxJitter = (long) (ttlMillis * JITTER_PERCENTAGE);
        return ThreadLocalRandom.current().nextLong(-maxJitter, maxJitter + 1);
    }

    private record CacheEntry(WeatherData response, long expiryTime) {
    }
}
