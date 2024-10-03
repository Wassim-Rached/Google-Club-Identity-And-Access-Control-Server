package com.example.usermanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class HealthCheckResponse {
    private String status;
    private Map<String, DependencyStatus> dependencies = new HashMap<>();
}
