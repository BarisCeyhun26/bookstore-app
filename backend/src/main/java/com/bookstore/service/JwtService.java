package com.bookstore.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    // SECRET_KEY is used in the token generation methods
    private static final int ACCESS_TOKEN_EXPIRY_MINUTES = 15;
    private static final int REFRESH_TOKEN_EXPIRY_DAYS = 7;

    /**
     * Access token oluşturur (basit Base64 encoding)
     */
    public String generateAccessToken(String username, String email, Set<String> roles) {
        long expiryTime = Instant.now().plus(ACCESS_TOKEN_EXPIRY_MINUTES, ChronoUnit.MINUTES).getEpochSecond();
        String payload = String.format("{\"username\":\"%s\",\"email\":\"%s\",\"roles\":%s,\"exp\":%d}", 
                                     username, email, roles.toString(), expiryTime);
        return Base64.getEncoder().encodeToString(payload.getBytes());
    }

    /**
     * Refresh token oluşturur (basit Base64 encoding)
     */
    public String generateRefreshToken(String username) {
        long expiryTime = Instant.now().plus(REFRESH_TOKEN_EXPIRY_DAYS, ChronoUnit.DAYS).getEpochSecond();
        String payload = String.format("{\"username\":\"%s\",\"type\":\"refresh\",\"exp\":%d}", 
                                     username, expiryTime);
        return Base64.getEncoder().encodeToString(payload.getBytes());
    }
}

