package com.epamlearning.reportmicroservice.security;

import com.epamlearning.reportmicroservice.controllers.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
public class JWTFilter
        extends OncePerRequestFilter
{

    @Value("${jwt.secret}")
    private String secret;

    public SecretKey generalKey() {
        byte[] encodeKey = Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(encodeKey);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            if (authHeader.isBlank()) {
                sendErrorResponse(response, "Token is blank.");
                return;
            }
            String token = authHeader.substring(7);
            try {
                Jws<Claims> claims = Jwts.parser().verifyWith(generalKey()).build().parseSignedClaims(token);
                String username = claims.getPayload().getSubject();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                sendErrorResponse(response, "Token expired.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse(message);
        String jsonError = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonError);
    }

}
