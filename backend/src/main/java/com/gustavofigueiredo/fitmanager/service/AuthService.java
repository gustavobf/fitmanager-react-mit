package com.gustavofigueiredo.fitmanager.service;

import com.gustavofigueiredo.fitmanager.dto.auth.LoginRequest;
import com.gustavofigueiredo.fitmanager.dto.auth.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final SecretKey secretKey;
    private final long expirationHours;

    public AuthService(@Value("${app.auth.jwt-secret}") String secret,
            @Value("${app.auth.jwt-expiration-hours}") long expirationHours) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationHours = expirationHours;
    }

    public LoginResponse login(LoginRequest request) {
        Instant now = Instant.now();
        Instant expiration = now.plus(expirationHours, ChronoUnit.HOURS);
        String token = Jwts.builder()
                .subject(request.login().trim())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
        return new LoginResponse(token, request.login().trim(), expiration);
    }

    public boolean tokenValido(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (RuntimeException exception) {
            return false;
        }
    }
}