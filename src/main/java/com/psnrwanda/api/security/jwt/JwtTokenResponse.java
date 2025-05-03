package com.psnrwanda.api.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT token response model for authentication endpoints
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenResponse {
    
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
} 