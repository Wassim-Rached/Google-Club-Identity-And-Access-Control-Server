package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.permissions.SimplePermissionDTO;
import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.interfaces.services.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<SimplePermissionDTO>> getAllPermissions() {
        List<SimplePermissionDTO> permissions = permissionService.getAllPermissions().stream().map(SimplePermissionDTO::new).toList();
        return ResponseEntity.ok(permissions);
    }

    @PostMapping
    public ResponseEntity<UUID> createPermission(@RequestBody Permission permission) {
        permissionService.savePermission(permission);
        return ResponseEntity.status(201).body(permission.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimplePermissionDTO> getPermission(@PathVariable UUID id) {
        SimplePermissionDTO permission = new SimplePermissionDTO(permissionService.getPermission(id));
        return ResponseEntity.ok(permission);
    }

}
