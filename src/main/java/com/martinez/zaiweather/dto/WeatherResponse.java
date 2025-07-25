package com.martinez.zaiweather.dto;

public class WeatherResponse {
    private final double temperatureCelsius;
    private final double windSpeed;

    public WeatherResponse(double temperatureCelsius, double windSpeedKph) {
        this.temperatureCelsius = temperatureCelsius;
        this.windSpeed = windSpeedKph;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}