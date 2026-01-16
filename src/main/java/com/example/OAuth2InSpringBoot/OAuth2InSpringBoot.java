package com.example.OAuth2InSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OAuth2InSpringBoot {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2InSpringBoot.class, args);
	}

	@Configuration
	class RestTemplateConfig{
		@Bean 
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}
	
}
