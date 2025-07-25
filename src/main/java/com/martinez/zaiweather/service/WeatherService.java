package com.martinez.zaiweather.service;

import com.martinez.zaiweather.dto.WeatherResponse;

public interface WeatherService {
    WeatherResponse getWeatherForCity(String city);
}