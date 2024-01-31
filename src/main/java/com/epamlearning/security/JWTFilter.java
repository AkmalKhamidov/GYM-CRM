package com.epamlearning.security;

import com.epamlearning.controllers.exception.ErrorResponse;
import com.epamlearning.services.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JWTFilter(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
        } else {
            String token = authHeader.substring(7);
            try {
                if (jwtUtil.validateToken(token)) {
                    UserDetails foundUser = userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
                    updateContext(foundUser, request);
                    filterChain.doFilter(request, response);
                }
            } catch (ExpiredJwtException e) {
                log.info(e.getMessage());
            }
        }
    }


    private void updateContext(UserDetails foundUser, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(foundUser, null, foundUser.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
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
