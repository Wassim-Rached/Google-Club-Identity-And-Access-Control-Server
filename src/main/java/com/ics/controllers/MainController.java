package com.ics.controllers;

import com.ics.dto.DependencyStatus;
import com.ics.dto.HealthCheckResponse;
import com.ics.enums.Authority;
import com.ics.repositories.PermissionRepository;
import com.ics.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainController {

    @Value("${app.depServers}")
    private List<String> depServers;

    private final RestTemplate restTemplate;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

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

    @GetMapping("/api/authorities")
    public ResponseEntity<List<String>> authorities() {
        List<String> authorityNames = Arrays.stream(Authority.values())
                                            .map(Authority::getAuthority)
                                            .collect(Collectors.toList());
        return new ResponseEntity<>(authorityNames, HttpStatus.OK);
    }

    @PostMapping("/api/authorities")
    public ResponseEntity<HashMap<UUID,String>> authorities(@RequestBody String[] authorities) {
        if (authorities == null) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
        }
        List<String> roles = Arrays.stream(authorities)
                .filter(authority -> authority.contains(".role."))
                .toList();
        List<String> permissions = Arrays.stream(authorities)
                .filter(authority -> authority.contains(".perm."))
                .toList();

        HashMap<UUID, String> authorityMap = new HashMap<>();

        roleRepository.findByPublicNames(roles).forEach(role -> authorityMap.put(role.getId(), role.getPublicName()));
        permissionRepository.findByPublicNames(permissions).forEach(permission -> authorityMap.put(permission.getId(), permission.getPublicName()));

        return new ResponseEntity<>(authorityMap, HttpStatus.OK);
    }
}
