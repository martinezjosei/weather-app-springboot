package com.martinez.zaiweather;

import com.martinez.zaiweather.cache.WeatherCache;
import com.martinez.zaiweather.client.WeatherClient;
import com.martinez.zaiweather.domain.Weather;
import com.martinez.zaiweather.dto.WeatherData;
import com.martinez.zaiweather.dto.WeatherResponse;
import com.martinez.zaiweather.service.WeatherServiceImpl;
import com.martinez.zaiweather.util.WeatherMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @Mock
    WeatherClient client1;

    @Mock
    WeatherClient client2;

    @Spy
    @InjectMocks
    WeatherServiceImpl weatherService = new WeatherServiceImpl(List.of());

    String city = "Manila";
    WeatherData sampleData = new WeatherData("mockProvider", city);


    @BeforeEach
    void setup() {
        WeatherCache.setTtlMillis(5 * 60 * 1000); // optional: reset TTL
        WeatherCache.get(city); // clear existing
        sampleData.setTemperature(30.5);
        sampleData.setWindSpeed(10.2);

        // Clear any cache interference
        WeatherCache.clearAll();


    }

    @AfterEach
    void teardown() {
        WeatherCache.get(city); // simulate cache cleanup
    }

    @Test
    void testGetWeather_CacheHit() {
        WeatherCache.put(city, sampleData);

        WeatherResponse response = weatherService.getWeatherForCity(city);

        // public record WeatherResponse(double temperatureCelsius, double windSpeed)
        assertNotNull(response);
        assertEquals(30.5, response.getTemperatureCelsius());
        assertEquals(10.2, response.getWindSpeed());
    }

    @Test
    void testGetWeather_FirstClientSuccess() {
        WeatherCache.get(city); // clear cache
        List<WeatherClient> clients = Collections.singletonList(client1);
        weatherService = new WeatherServiceImpl(clients);

        when(client1.getWeatherData(city)).thenReturn(sampleData);

        WeatherResponse response = weatherService.getWeatherForCity(city);

        assertNotNull(response);
        assertEquals(30.5, response.getTemperatureCelsius());
        verify(client1, times(1)).getWeatherData(city);
    }

    @Test
    void testGetWeather_FirstClientFails_SecondClientSuccess() {
        when(client1.getWeatherData(city)).thenThrow(new RuntimeException("API down"));
        when(client2.getWeatherData(city)).thenReturn(sampleData);

        weatherService = new WeatherServiceImpl(Arrays.asList(client1, client2));

        WeatherResponse response = weatherService.getWeatherForCity(city);

        assertNotNull(response);
        assertEquals(30.5, response.getTemperatureCelsius());
        verify(client1, times(1)).getWeatherData(city);
        verify(client2, times(1)).getWeatherData(city);
    }

    @Test
    void testGetWeather_AllClientsFail() {
        when(client1.getWeatherData(city)).thenThrow(new RuntimeException("API1 down"));
        when(client2.getWeatherData(city)).thenThrow(new RuntimeException("API2 down"));

        weatherService = new WeatherServiceImpl(Arrays.asList(client1, client2));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> weatherService.getWeatherForCity(city));
        assertTrue(ex.getMessage().contains("Unable to fetch weather data for city"));
    }

    @Test
    void testToWeatherObject() {
        Weather weather = weatherService.toWeatherObject(sampleData);

        assertNotNull(weather);
        assertEquals("mockProvider", weather.getProvider());
        assertEquals("Manila", weather.getLocation());
    }

    @Test
    void testProcessWeatherObject() {
        Weather weather = new Weather();
        weather.setProvider("mockProvider");
        weather.setLocation("Manila");
        weather.setTemperature(30.5);
        weather.setWindSpeed(10.2);

        WeatherResponse response = weatherService.processWeatherObject(weather);

        assertEquals(30.5, response.getTemperatureCelsius());
        assertEquals(10.2, response.getWindSpeed());
    }
}
