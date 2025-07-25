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

## Trade-offs 
  If I were to spend additional time on the task, I would have: 
 - Added security OAuth? 
 - Added versioning for the endpoints: GET http://api.example.com/api/v1/weather?city=Sydney 
 - Added ability to send other parameters like temperature units, wind speed units, etc. 
 - Added OWASP/ESAPI jars to check for CVE vulnerabilities
 - Run a coverage report on JACOCO
 - Better tiers/layer separation of client classes vs service classes 
 - Placeholder for database hook in the future using JPA for example 
 - I chose to hardcode the fallback logic for switching between WeatherStack and OpenWeather 
    rather than implementing a more flexible or fault-tolerant strategy (e.g., circuit breakers or retry logic using Resilience4j or Spring Retry)
 - I used in-memory caching instead of introducing Redis or another persistent caching layer to keep the setup light.
 - For simplicity, I kept the data mapping flat instead of creating deep DTOs or domain models for nested weather API responses. 
 - Things I left out (given the scope/time):
   Security: I didn't include input validation, rate limiting, or protection against abuse (e.g., header sanitization, user input validation).
 - Tests: I included unit tests for key components only. 
 - CI/CD pipeline or Dockerization: I kept it local, though containerizing and deploying via GitHub Actions or similar would be standard in a real project.
 - Documentation: I included basic comments and a README, but would normally add OpenAPI (Swagger) documentation for the API interface.
 - I include a nice diagram based on the UML specification. 
 - Add a proper interface-based abstraction layer for the weather providers, with a registry/factory pattern so that new providers could be swapped in without changing controller logic.
 - Introduce Resilience4j to handle retries, fallbacks, timeouts, and circuit breakers around external API calls.
 - Add logging and metrics (via SLF4J + Micrometer) for observability.
 - Containerize the app with Docker, and possibly deploy to a cloud provider like AWS, Azure, etc. 

