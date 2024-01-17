package com.epamlearning.security;

import com.epamlearning.controllers.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Set;

@Component
@WebFilter(urlPatterns = "/*")
public class JWTFilter implements Filter {

    private static final Set<String> ALLOWED_PATHS = Set.of(
            "",
            "/auth/login",
            "/trainee/register",
            "/trainer/register",
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-initializer.js",
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/index.css",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/favicon-32x32.png",
            "/swagger-ui/favicon-16x16.png");
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    public void init(FilterConfig filterConfig) {
        // Initialization code can go here if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("/+$", "");
        boolean loggedIn = false;

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (new JWTUtil().validateTokenRetrieveUsername(token) != null) {
                loggedIn = true;
            }
        }
        if(loggedIn || isPathAllowed(path)){
            filterChain.doFilter(request, response);
        } else {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");
            sendErrorResponse(response, "Invalid access token");
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

    @Override
    public void destroy() {
        // Cleanup code can go here if needed
    }
}
