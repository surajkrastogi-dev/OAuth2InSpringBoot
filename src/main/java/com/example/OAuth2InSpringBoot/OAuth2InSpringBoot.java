package com.example.OAuth2InSpringBoot;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
	
	@Configuration
	class PasswordEncoderConfig{
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
	}
	
	@Configuration
	class CORSConfiguration{
		@Bean
		public CorsConfigurationSource corsConfigurationSource() {
		    CorsConfiguration config = new CorsConfiguration();
		    config.setAllowedOrigins(List.of("http://localhost:3000"));
		    config.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
		    config.setAllowedHeaders(List.of("*"));
		    config.setAllowCredentials(true); // ✅ very important for cookies

		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    source.registerCorsConfiguration("/**", config);
		    return source;
		}
	}
	
}
