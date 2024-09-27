package com.example.usermanagement.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String home() {
        return "Identity and Access Control Service Is Up and Running";
    }

    @GetMapping("/api/health")
    public String health() {
        return "Service is healthy";
    }
}
