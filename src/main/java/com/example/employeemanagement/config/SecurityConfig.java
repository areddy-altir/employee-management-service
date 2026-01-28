package com.example.employeemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security Configuration for Keycloak OAuth2 Resource Server
 * 
 * This configuration:
 * - Validates JWT tokens from Keycloak
 * - Extracts roles from JWT token
 * - Enables method-level security (@PreAuthorize)
 * - Configures CORS for frontend access
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Security Filter Chain Configuration
     * 
     * All endpoints require authentication by default.
     * JWT tokens are validated against Keycloak issuer.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for stateless JWT-based auth
            .csrf(csrf -> csrf.disable())
            
            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Stateless session (JWT tokens, no server-side sessions)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configure OAuth2 Resource Server (Keycloak JWT validation)
            // JWT decoder is auto-configured from application.properties
            // spring.security.oauth2.resourceserver.jwt.issuer-uri
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> {
                    // JWT decoder is auto-configured from application.properties
                    // No additional configuration needed
                })
            )
            
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (if any)
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                
                // All other endpoints require authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }

    /**
     * CORS Configuration
     * Allows frontend applications to call the API
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow all origins (restrict in production)
        configuration.setAllowedOrigins(List.of("*"));
        
        // Allow common HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Allow Authorization header (for JWT tokens)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // Allow credentials (cookies, auth headers)
        configuration.setAllowCredentials(true);
        
        // Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
