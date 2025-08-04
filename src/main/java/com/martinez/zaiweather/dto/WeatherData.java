package com.martinez.zaiweather.dto;

public class WeatherData {
    private final String provider;
    private final String location;
    private double temperature;
    private double windSpeed;

    public WeatherData(String provider, String location) {
        this.provider = provider;
        this.location = location;
    }

    public String getProvider(){
        return provider;
    }

    public String getLocation(){
        return location;
    }


    public void setTemperature(double temperature){
        this.temperature = temperature;
    }


    public double getTemperature(){
        return temperature;
    }


    public void setWindSpeed(double windSpeed){
        this.windSpeed = windSpeed;
    }

    public double getWindSpeed(){
        return windSpeed;
    }

}