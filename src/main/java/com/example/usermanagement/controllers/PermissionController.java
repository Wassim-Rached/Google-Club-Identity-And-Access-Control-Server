package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.StandardApiResponse;
import com.example.usermanagement.dto.permissions.SimplePermissionDTO;
import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.enums.StandardApiStatus;
import com.example.usermanagement.services.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<StandardApiResponse<List<SimplePermissionDTO>>> getAllPermissions() {
        List<SimplePermissionDTO> permissions = permissionService.getAllPermissions().stream().map(SimplePermissionDTO::new).toList();
        return ResponseEntity.ok(new StandardApiResponse<>(permissions));
    }

    @PostMapping
    public ResponseEntity<StandardApiResponse<UUID>> createPermission(@RequestBody Permission permission) {
        permissionService.savePermission(permission);
        return ResponseEntity.status(201).body(new StandardApiResponse<>(permission.getId()));
    }


}
