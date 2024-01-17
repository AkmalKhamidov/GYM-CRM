package com.epamlearning.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(
        basePackages = {"org.springdoc"}
)
@Import({SpringDocConfiguration.class,
        SpringDocWebMvcConfiguration.class,
        org.springdoc.webmvc.ui.SwaggerConfig.class,
        SwaggerUiConfigProperties.class,
        SwaggerUiOAuthProperties.class,
        JacksonAutoConfiguration.class})
public class OpenAPI30Configuration {
    private static final String SECURITY_SCHEME_NAME_BASIC = "basicAuth";
    private static final String SECURITY_SCHEME_NAME_BEARER = "bearerAuth";

    @Bean
    public OpenAPI customizeOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME_BASIC, createBasicSecurityScheme())
                        .addSecuritySchemes(SECURITY_SCHEME_NAME_BEARER, createBearerSecurityScheme()))
                .addSecurityItem(createBearerSecurityRequirement())
                .addSecurityItem(createBasicSecurityRequirement());
    }

    private SecurityScheme createBearerSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME_BEARER)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);
    }

    private SecurityScheme createBasicSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME_BASIC)
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .in(SecurityScheme.In.HEADER);
    }

    private SecurityRequirement createBearerSecurityRequirement() {
        SecurityRequirement requirement = new SecurityRequirement();
        requirement.addList(SECURITY_SCHEME_NAME_BEARER);
        return requirement;
    }

    private SecurityRequirement createBasicSecurityRequirement() {
        SecurityRequirement requirement = new SecurityRequirement();
        requirement.addList(SECURITY_SCHEME_NAME_BASIC);
        return requirement;
    }
}