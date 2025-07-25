package com.martinez.zaiweather.controller;

import com.martinez.zaiweather.service.WeatherService;
import com.martinez.zaiweather.dto.WeatherResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public WeatherResponse getWeather(@RequestParam(defaultValue = "Melbourne") String city) {
        return weatherService.getWeatherForCity(city);
    }
}