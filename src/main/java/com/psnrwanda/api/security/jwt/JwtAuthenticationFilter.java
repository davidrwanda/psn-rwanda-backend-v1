package com.psnrwanda.api.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter for validating tokens in requests
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenFactory tokenFactory;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        try {
            String jwt = getTokenFromRequest(request);
            
            if (StringUtils.hasText(jwt) && !isRefreshTokenEndpoint(request)) {
                // Ignore refresh tokens in regular authentication
                if (!tokenFactory.isRefreshToken(jwt)) {
                    String username = tokenFactory.getUsernameFromToken(jwt);
                    
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    if (tokenFactory.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = 
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
        }
        
        chain.doFilter(request, response);
    }
    
    /**
     * Get JWT token from request
     * @param request HTTP request
     * @return JWT token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
    
    /**
     * Check if this is a token refresh endpoint
     * @param request HTTP request
     * @return True if token refresh endpoint
     */
    private boolean isRefreshTokenEndpoint(HttpServletRequest request) {
        return request.getRequestURI().contains("/api/v1/auth/refresh");
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // All GET requests should bypass authentication checks
        if ("GET".equals(method)) {
            log.debug("Bypassing JWT filter for GET request: {}", uri);
            return true;
        }
        
        // Check for specific endpoints that don't require authentication
        boolean isAuthEndpoint = isAuthenticationEndpoint(request);
        log.debug("Request URI: {}, Method: {}, Is Auth Endpoint: {}", uri, method, isAuthEndpoint);
        return isAuthEndpoint;
    }
    
    /**
     * Check if this is an authentication endpoint (login, register) or a public endpoint
     */
    private boolean isAuthenticationEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // Authentication endpoints
        if (uri.contains("/api/v1/auth/login") || 
            uri.contains("/api/v1/auth/logout") ||
            uri.contains("/api/v1/auth/refresh") ||
            uri.contains("/api/v1/auth/test") ||
            uri.contains("/api/v1/users/register") ||
            uri.equals("/api/v1/test/public")) {
            return true;
        }
        
        // Test email endpoints
        if (uri.contains("/api/v1/test/email/")) {
            return true;
        }
        
        // Documentation endpoints
        if (uri.contains("/api/v1/docs") || 
            uri.contains("/api/v1/swagger-ui") || 
            uri.contains("/docs") || 
            uri.contains("/swagger-ui")) {
            return true;
        }
        
        // Public booking operations
        if (uri.contains("/api/v1/bookings/public") ||
            (uri.contains("/api/v1/bookings/track") && "POST".equals(method)) ||
            (uri.contains("/api/v1/bookings/documents/upload") && "POST".equals(method)) ||
            (uri.contains("/api/v1/documents/upload") && "POST".equals(method))) {
            return true;
        }
        
        return false;
    }
} 