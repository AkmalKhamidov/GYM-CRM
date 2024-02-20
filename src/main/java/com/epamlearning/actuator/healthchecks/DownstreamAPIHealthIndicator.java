package com.epamlearning.actuator.healthchecks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:downstream-service.properties")
@Endpoint(id = "downstream-service")
//@ConditionalOnEnabledHealthIndicator("downstream-service")
public class DownstreamAPIHealthIndicator implements HealthIndicator {

    RestTemplate restTemplate = new RestTemplate();
    Map<String, Object> details = new HashMap<>();

    @Value("${service.google}")
    private String url;

    @ReadOperation
    @Override
    public Health health() {
        details.put("URL", url);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            details.put("Status code", response.getStatusCode().value());
            details.put("Status", response.getStatusCode());
            return Health.up().withDetails(details).build();
        } catch (HttpClientErrorException e) {
            details.put("Status code", e.getStatusCode().value());
            details.put("Status", e.getStatusCode());
            details.put("Message", e.getMessage());
            return Health.down().withDetails(details).build();
        }
    }
}
