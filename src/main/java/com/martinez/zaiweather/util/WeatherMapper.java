package com.martinez.zaiweather.util;

import com.martinez.zaiweather.domain.Weather;
import com.martinez.zaiweather.dto.WeatherData;

public class WeatherMapper {
    public static Weather toWeather(WeatherData data) {
        Weather weather = new Weather();
        weather.setProvider(data.getProvider());
        weather.setLocation(data.getLocation());
        weather.setTemperature(data.getTemperature());
        weather.setWindSpeed(data.getWindSpeed());
        weather.setLocalDateTime(DateUtils.getLocalDateTime());

        return weather;
    }
}
