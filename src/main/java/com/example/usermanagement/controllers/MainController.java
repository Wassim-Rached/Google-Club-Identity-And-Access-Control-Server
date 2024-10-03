package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.DependencyStatus;
import com.example.usermanagement.dto.HealthCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MainController {

    @Value("${app.depServers}")
    private List<String> depServers;

    private final RestTemplate restTemplate;

    @GetMapping("/")
    public String home() {
        return "Identity and Access Control Service Is Up and Running";
    }

    @GetMapping("/api/health")
    public ResponseEntity<HealthCheckResponse> health() {
        HealthCheckResponse healthCheckResponse = new HealthCheckResponse();
        healthCheckResponse.setStatus("healthy");

        for (String depServer : depServers) {
            try {
                ResponseEntity<Map> response = restTemplate.getForEntity(depServer + "/api/health", Map.class);
                response.getStatusCode().is2xxSuccessful(); // Raise an exception for error status codes
                healthCheckResponse.getDependencies().put(depServer, new DependencyStatus("healthy", response.getStatusCode().value()));
            } catch (Exception e) {
                healthCheckResponse.setStatus("unhealthy");
                healthCheckResponse.getDependencies().put(depServer, new DependencyStatus("unhealthy", 500, e.getMessage()));
            }
        }

        HttpStatus statusCode = healthCheckResponse.getStatus().equals("healthy") ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(healthCheckResponse, statusCode);
    }
}
