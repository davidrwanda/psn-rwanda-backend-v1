package com.psnrwanda.api.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psnrwanda.api.exception.ApiError;
import com.psnrwanda.api.exception.ErrorResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Entry point for handling unauthorized requests
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                        AuthenticationException authException) throws IOException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        String errorMessage = "Unauthorized: " + authException.getMessage();
        
        // Add debug information
        if (request.getRequestURI().contains("/api/v1/auth/login")) {
            errorMessage = "Login failed. Please check your credentials.";
        }
        
        ApiError error = ErrorResponseFactory.createAuthError(
                errorMessage, 
                request.getRequestURI()
        );
        
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
} 