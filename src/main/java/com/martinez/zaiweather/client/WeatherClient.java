package com.martinez.zaiweather.client;

import com.martinez.zaiweather.dto.WeatherData;

public interface WeatherClient {

    String getProviderName();

    WeatherData getWeatherData(String location); // rename to getWeatherData(...)
}


