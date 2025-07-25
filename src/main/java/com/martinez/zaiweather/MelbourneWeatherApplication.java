package com.martinez.zaiweather;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties()
public class MelbourneWeatherApplication {

	public static void main(String[] args) {
		Dotenv dotenv =  Dotenv.configure().ignoreIfMissing().load();

		// Inject .env into system properties so Spring can access it
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(MelbourneWeatherApplication.class, args);
	}

}
