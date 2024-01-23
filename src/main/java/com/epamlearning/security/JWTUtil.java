package com.epamlearning.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token.access.expiration}")
    private long expirationTimeMillis; // 2 hours
    @Value("${jwt.token.refresh.expiration}")
    private long refreshExpirationTimeMillis; // 48 hours

    public String generateAccessToken(String username) {
        return generateToken(username, expirationTimeMillis);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshExpirationTimeMillis);
    }

    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpirationDateFromToken(String token) {
        return extractClaims(token).getExpiration();
    }

    public Date extractIssuedAtDateFromToken(String token) {
        return extractClaims(token).getIssuedAt();
    }

    public String extractUsername(String token) {
        return extractClaims(token).get("username", String.class);
    }

    public boolean validateToken(String token) {
        return !extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return (Claims) Jwts.parser().verifyWith(generalKey()).build().parse(token).getPayload();
    }

    private String generateToken(String username, long expirationTime) {
        return Jwts.builder()
                .signWith(generalKey())
                .claims().add("username", username)
                .subject("user details")
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .issuer("epamlearning.com")
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .compact();
    }

    public SecretKey generalKey() {
        byte[] encodeKey = Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(encodeKey);
    }


}
