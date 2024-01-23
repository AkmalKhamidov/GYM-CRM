package com.epamlearning.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = {@Server(url = "/",description = "server URL http://localhost")},
        info = @Info(title = "GYM CRM API", version = "1.0", description = "Gym CRM API Information")
)
public class OpenAPI30Config {
    private static final String SECURITY_SCHEME_NAME_BEARER = "bearerAuth";

    @Bean
    public OpenAPI customizeOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME_BEARER, createBearerSecurityScheme()))
                .addSecurityItem(createBearerSecurityRequirement());
    }

    private SecurityScheme createBearerSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME_BEARER)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);
    }

    private SecurityRequirement createBearerSecurityRequirement() {
        SecurityRequirement requirement = new SecurityRequirement();
        requirement.addList(SECURITY_SCHEME_NAME_BEARER);
        return requirement;
    }

}