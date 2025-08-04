package com.martinez.zaiweather.dto;

import java.util.Objects;

public class WeatherResponse {

    private double temperatureCelsius;
    private double windSpeed;

    public WeatherResponse() {
        // no-args constructor
    }


    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "temperatureCelsius=" + temperatureCelsius +
                ", windSpeed=" + windSpeed +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof WeatherResponse that)) return false;
//        return Double.compare(that.temperatureCelsius, temperatureCelsius) == 0 &&
//                Double.compare(that.windSpeed, windSpeed) == 0;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(temperatureCelsius, windSpeed);
//    }
}
