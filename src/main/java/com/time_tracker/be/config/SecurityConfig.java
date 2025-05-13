package com.time_tracker.be.config;

import com.time_tracker.be.exception.ForbiddenException;
import com.time_tracker.be.exception.NotAuthorizedException;
import com.time_tracker.be.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// ...

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ForbiddenException forbiddenException;
    private final NotAuthorizedException notAuthorizedException;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          ForbiddenException forbiddenException,
                          NotAuthorizedException notAuthorizedException
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.forbiddenException = forbiddenException;
        this.notAuthorizedException = notAuthorizedException;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // atau cukup .cors()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/1.0/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(forbiddenException)
                        .authenticationEntryPoint(notAuthorizedException)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}