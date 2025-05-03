package com.psnrwanda.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Factory for creating and validating JWT tokens
 */
@Component
public class JwtTokenFactory {
    
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    
    @Value("${security.jwt.token.access-token-expire-length}")
    private long accessTokenValidity;
    
    @Value("${security.jwt.token.refresh-token-expire-length}")
    private long refreshTokenValidity;
    
    /**
     * Generate a JWT access token
     * @param userDetails User details
     * @return JWT token
     */
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenValidity, TokenType.ACCESS);
    }
    
    /**
     * Generate a JWT refresh token
     * @param userDetails User details
     * @return JWT token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenValidity, TokenType.REFRESH);
    }
    
    /**
     * Generate JWT tokens response after authentication
     * @param authentication Authentication object
     * @return JWT token response with access and refresh tokens
     */
    public JwtTokenResponse generateTokenResponse(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        String accessToken = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);
        
        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenValidity / 1000)
                .build();
    }
    
    /**
     * Get username from token
     * @param token JWT token
     * @return Username
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * Get expiration date from token
     * @param token JWT token
     * @return Expiration date
     */
    public Date getExpirationFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * Get token type from token
     * @param token JWT token
     * @return Token type
     */
    public TokenType getTokenTypeFromToken(String token) {
        String type = getClaimFromToken(token, claims -> claims.get("type", String.class));
        return TokenType.valueOf(type);
    }
    
    /**
     * Validate token
     * @param token JWT token
     * @param userDetails User details
     * @return True if valid
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Check if token is for refresh
     * @param token JWT token
     * @return True if refresh token
     */
    public boolean isRefreshToken(String token) {
        return getTokenTypeFromToken(token) == TokenType.REFRESH;
    }
    
    /**
     * Extract claim from token
     * @param token JWT token
     * @param claimsResolver Claims resolver function
     * @param <T> Claim type
     * @return Claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Get all claims from token
     * @param token JWT token
     * @return Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Check if token is expired
     * @param token JWT token
     * @return True if expired
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * Generate token
     * @param userDetails User details
     * @param validity Validity period
     * @param tokenType Token type
     * @return JWT token
     */
    private String generateToken(UserDetails userDetails, long validity, TokenType tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", tokenType.name());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Get signing key
     * @return Signing key
     */
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Token type enum
     */
    public enum TokenType {
        ACCESS,
        REFRESH
    }
} 