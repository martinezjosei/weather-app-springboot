package com.martinez.zaiweather.domain;

public class Weather {

        private String provider;
        private String location;
        private double temperature;
        private double windSpeed;
        // Possibly: LocalDateTime timestamp, internal metadata, etc.
        private String localDateTime;


    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "provider='" + provider + '\'' +
                ", location='" + location + '\'' +
                ", temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                ", localDateTime='" + localDateTime + '\'' +
                '}';
    }
}
