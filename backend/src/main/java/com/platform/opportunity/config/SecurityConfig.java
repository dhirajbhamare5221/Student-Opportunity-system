package com.platform.opportunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for OpportunityHub.
 * 
 * In this foundation phase:
 * 1. BCryptPasswordEncoder bean is exposed for secure password hashing.
 * 2. Static web resources and health APIs are accessible to allow initial testing.
 * 3. H2 console frame options are enabled for database inspection.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt is a strong, industry-standard cryptographic hash function
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simple REST API testing
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Allows H2 web console
            .authorizeHttpRequests(auth -> auth
                // Allow all static frontend assets and health endpoints
                .requestMatchers("/", "/index.html", "/login.html", "/profile.html",
                                 "/opportunity-detail.html", "/bookmarks.html", 
                                 "/dashboard.html", "/admin.html").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .requestMatchers("/api/health", "/api/status", "/api/system/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // In Phase 1, permit all API requests so initial setup can be validated end-to-end
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
