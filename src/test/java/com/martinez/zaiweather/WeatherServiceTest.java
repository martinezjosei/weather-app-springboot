package com.martinez.zaiweather;

import com.martinez.zaiweather.cache.WeatherCache_OLD;
import com.martinez.zaiweather.dto.WeatherResponse;
import com.martinez.zaiweather.client.WeatherClient_OLD;
import com.martinez.zaiweather.service.WeatherServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceTest {

    @Mock
    private WeatherClient_OLD weatherClient;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        WeatherCache_OLD.setCache(null); // Clear cache between tests
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testFetchFromWeatherStackSuccess_Default() {
        WeatherResponse mockResponse = new WeatherResponse(15.7, 12.3);
        when(weatherClient.fetchFromWeatherStack("melbourne")).thenReturn(mockResponse);

        WeatherResponse result = weatherService.getWeatherForCity("melbourne");

        assertEquals(15.7, result.temperatureCelsius());
        assertEquals(12.3, result.windSpeed());
        verify(weatherClient, times(1)).fetchFromWeatherStack("melbourne");
        verify(weatherClient, never()).fetchFromOpenWeather(anyString());
    }

    @Test
    void testFetchFromWeatherStackSuccess_Sydney() {
        WeatherResponse mockResponse = new WeatherResponse(11.1, 22.2);
        when(weatherClient.fetchFromWeatherStack("Sydney")).thenReturn(mockResponse);

        WeatherResponse result = weatherService.getWeatherForCity("Sydney");

        assertEquals(11.1, result.temperatureCelsius());
        assertEquals(22.2, result.windSpeed());
        verify(weatherClient, times(1)).fetchFromWeatherStack("Sydney");
        verify(weatherClient, never()).fetchFromOpenWeather(anyString());
    }

    @Test
    void testWeatherStackFailsThenOpenWeatherSucceeds() {
        when(weatherClient.fetchFromWeatherStack("melbourne"))
                .thenThrow(new RuntimeException("WeatherStack is down"));
        WeatherResponse fallbackResponse = new WeatherResponse(18.0, 12.0);
        when(weatherClient.fetchFromOpenWeather("melbourne"))
                .thenReturn(fallbackResponse);

        WeatherResponse result = weatherService.getWeatherForCity("melbourne");

        assertEquals(18.0, result.temperatureCelsius());
        assertEquals(12.0, result.windSpeed());
        verify(weatherClient).fetchFromWeatherStack("melbourne");
        verify(weatherClient).fetchFromOpenWeather("melbourne");
    }

    @Test
    void testBothProvidersFailReturnsStaleCache() {
        WeatherResponse cached = new WeatherResponse(19.0, 14.0);
        WeatherCache_OLD.setCache(cached);

        when(weatherClient.fetchFromWeatherStack("melbourne"))
                .thenThrow(new RuntimeException("Primary failed"));
        when(weatherClient.fetchFromOpenWeather("melbourne"))
                .thenThrow(new RuntimeException("Fallback failed"));

        WeatherResponse result = weatherService.getWeatherForCity("melbourne");

        assertEquals(19.0, result.temperatureCelsius());
        assertEquals(14.0, result.windSpeed());
    }

    @Test
    void testBothProvidersFailNoCacheThrowsException() {
        WeatherCache_OLD.setCache(null);

        when(weatherClient.fetchFromWeatherStack("melbourne"))
                .thenThrow(new RuntimeException("No weather provider is available at this time"));
        when(weatherClient.fetchFromOpenWeather("melbourne"))
                .thenThrow(new RuntimeException("No weather provider is available at this time"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> weatherService.getWeatherForCity("melbourne"));
        assertTrue(ex.getMessage().contains("No weather provider is available at this time"));
    }
}