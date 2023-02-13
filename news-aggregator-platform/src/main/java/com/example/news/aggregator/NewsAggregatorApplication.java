package com.example.news.aggregator;

import java.time.Clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "Employees API", version = "2.0", description = "Employees Information"))
//@EnableSwagger2
public class NewsAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsAggregatorApplication.class, args);
	}

	
	@Bean
	Clock systemClock() {
		return Clock.systemUTC();
	}
	

}
