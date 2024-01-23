package com.epamlearning.security;

import com.epamlearning.controllers.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@WebFilter(urlPatterns = "/*")
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final Set<String> ALLOWED_PATHS = Set.of(
            "",
            "/actuator/**",
            "/auth/login",
            "/auth/refresh",
            "/trainee/register",
            "/trainer/register",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-initializer.js",
            "/v3/api-docs",
            "/api-docs",
            "/v3/api-docs/swagger-config",
            "/api-docs/swagger-config",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/index.css",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/favicon-32x32.png",
            "/swagger-ui/favicon-16x16.png");
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("/+$", "");
        boolean loggedIn = false;

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try{
                if(jwtUtil.validateToken(token)){
                    loggedIn = true;
                }
            } catch (ExpiredJwtException e) {
                sendErrorResponse(response, e.getMessage());
                return;
            }
        }
        if(loggedIn || isPathAllowed(path)){
            filterChain.doFilter(request, response);
        } else {
            sendErrorResponse(response, "Invalid access/refresh token");
        }
    }


    private boolean isPathAllowed(String path) {
        for (String allowedPath : ALLOWED_PATHS) {
            if (pathMatcher.match(allowedPath, path)) {
                return true;
            }
        }
        return false;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse(message);
        String jsonError = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonError);
    }

}
