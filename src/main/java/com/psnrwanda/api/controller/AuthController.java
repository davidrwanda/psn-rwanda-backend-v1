package com.psnrwanda.api.controller;

import com.psnrwanda.api.dto.UserDto;
import com.psnrwanda.api.security.jwt.JwtTokenFactory;
import com.psnrwanda.api.security.jwt.JwtTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication operations
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenFactory tokenFactory;

    /**
     * User login endpoint
     *
     * @param loginDto the login credentials
     * @return JWT token response
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and generate access token")
    public ResponseEntity<JwtTokenResponse> login(@Valid @RequestBody UserDto.LoginDto loginDto) {
        log.info("Processing login request for user: {}", loginDto.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            JwtTokenResponse tokenResponse = tokenFactory.generateTokenResponse(authentication);
            
            log.info("User {} successfully authenticated", loginDto.getUsername());
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            log.error("Authentication failed for user: {}, error: {}", loginDto.getUsername(), e.getMessage());
            throw e;
        }
    }

    /**
     * Token refresh endpoint
     *
     * @param authentication current authentication (with refresh token)
     * @return new JWT token response
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Generate new access token using refresh token")
    public ResponseEntity<JwtTokenResponse> refreshToken(Authentication authentication) {
        log.info("Processing token refresh for user: {}", authentication.getName());
        
        JwtTokenResponse tokenResponse = tokenFactory.generateTokenResponse(authentication);
        
        log.info("Token refreshed for user: {}", authentication.getName());
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * Logout endpoint
     *
     * @return success response
     */
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Invalidate the user session")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    /**
     * Test endpoint to check authentication
     *
     * @return success response
     */
    @PostMapping("/test")
    @Operation(summary = "Test endpoint", description = "Test endpoint for checking authentication")
    public ResponseEntity<String> test() {
        log.info("Public test endpoint accessed");
        return ResponseEntity.ok("Test endpoint working correctly");
    }
} 