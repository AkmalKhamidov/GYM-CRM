package com.epamlearning.security;

import com.epamlearning.controllers.exception.ErrorResponse;
import com.epamlearning.services.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

  private final JWTFilter jwtFilter;

  @Value("${cors.allowed-origins}")
  private List<String> list;
  private final UserDetailsServiceImpl userDetailsService;

  ObjectMapper objectMapper = new ObjectMapper();


  @Autowired
  public SecurityConfig(JWTFilter jwtFilter, UserDetailsServiceImpl userDetailsService) {
    this.jwtFilter = jwtFilter;
    this.userDetailsService = userDetailsService;
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/auth/login",
                    "/auth/refresh",
                    "/trainee/register",
                    "/trainer/register"
                ).permitAll()
                .requestMatchers("/training/**",
                    "/auth/change-password",
                    "/auth/logout").hasAnyRole("TRAINEE", "TRAINER")
                .requestMatchers("/trainee/**").hasRole("TRAINEE")
                .requestMatchers("/trainer/**").hasRole("TRAINER")
                .anyRequest().authenticated()
        )
        .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
            .authenticationEntryPoint((request, response, authException) -> {
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              objectMapper.writeValue(response.getWriter(),
                  new ErrorResponse(authException.getMessage()));
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              response.setStatus(HttpServletResponse.SC_FORBIDDEN);
              objectMapper.writeValue(response.getWriter(),
                  new ErrorResponse(accessDeniedException.getMessage()));
            })
        )
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .userDetailsService(userDetailsService)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(list);
    configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name()));
    configuration.setAllowedHeaders(Arrays.asList(HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
