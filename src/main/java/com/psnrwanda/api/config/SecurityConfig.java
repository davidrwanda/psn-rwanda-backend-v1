package com.psnrwanda.api.config;

import com.psnrwanda.api.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration using Spring Security
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint unauthorizedHandler;
    private final AppProperties appProperties;
    
    /**
     * Security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Authentication and registration endpoints
                    auth.requestMatchers("/api/v1/auth/login", 
                                        "/api/v1/auth/logout", 
                                        "/api/v1/auth/refresh", 
                                        "/api/v1/auth/test", 
                                        "/api/v1/users/register").permitAll()
                    
                    // Documentation
                    .requestMatchers("/api/v1/docs/**", 
                                     "/api/v1/swagger-ui/**", 
                                     "/docs/**", 
                                     "/swagger-ui/**").permitAll()
                    
                    // Test endpoint
                    .requestMatchers("/api/v1/test/public").permitAll()
                    
                    // All GET operations are publicly accessible
                    .requestMatchers(HttpMethod.GET).permitAll()
                    
                    // Public POST operations for non-authenticated users
                    .requestMatchers(HttpMethod.POST, "/api/v1/bookings/public").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/bookings/track").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/bookings/documents/upload").permitAll()
                    
                    // Admin-only operations
                    .requestMatchers(HttpMethod.POST, "/api/v1/services/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/services/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/services/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/v1/services/**").hasRole("ADMIN")
                    .requestMatchers("/api/v1/reports/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/company/**").hasRole("ADMIN")
                    
                    // Any other endpoints require authentication
                    .anyRequest().authenticated();
                })
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    
    /**
     * Authentication provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    /**
     * Authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    /**
     * Password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList(appProperties.getCors().getAllowedOrigins().split(", ")));
        configuration.setAllowedMethods(
                Arrays.asList(appProperties.getCors().getAllowedMethods().split(", ")));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 