package com.example.OAuth2InSpringBoot.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.OAuth2InSpringBoot.ServiceImpl.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtAuthFilter jwtFilter;
    @Autowired private CustomUserDetailsService userDetailsService;
    
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

	@Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
    		OAuth2SuccessHandler oAuth2SuccessHandler,OAuth2LogoutSuccessHandler oAuth2LogoutSuccessHandler
    		) throws Exception {

        http.csrf(csrf -> csrf.disable())
        	.sessionManagement(session ->session
        		.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/oauth/google/**").permitAll()
                .requestMatchers("/api/test/**").hasAnyRole("USER","ADMIN")
                .anyRequest().authenticated()
//                	.anyRequest().permitAll()
            )
            .oauth2Login(oauth -> oauth.successHandler(oAuth2SuccessHandler) )
            .logout(logout -> logout.logoutUrl("/oauth/google/logout")
                    .logoutSuccessHandler(oAuth2LogoutSuccessHandler) )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

