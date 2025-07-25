# Weather App (Spring Boot)

This is a simple Spring Boot app that fetches weather data from WeatherStack and OpenWeather APIs.

## Features
- REST API with `@GetMapping` for current weather
- External API integration (WeatherStack / OpenWeather)
- Basic unit tests with JUnit and Mockito
- API Keys are pulled from the .env file (not included for security)

## How to Run
1. Clone the repo
2. Add your API keys in `.env` or `application.yml`
3. Run: `./mvnw spring-boot:run`

## Demo
Try `/weather?city=Sydney` once it's running.
I will default to 'Melbourne' if no city is entered. 
Units are set to metric. 

