package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.permissions.DetailedPermissionDTO;
import com.example.usermanagement.dto.permissions.GeneralPermissionDTO;
import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.interfaces.services.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    @GetMapping
    public ResponseEntity<Page<GeneralPermissionDTO>> searchPermissions(
            @RequestParam(required = false) String publicName,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<Permission> permissions = permissionService.SearchAndSortPermissions(publicName, sort, page, size, direction);
        Page<GeneralPermissionDTO> permissionDTOs = permissions.map(GeneralPermissionDTO::new);
        return ResponseEntity.ok(permissionDTOs);
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
    public ResponseEntity<DetailedPermissionDTO> getPermission(@PathVariable UUID id) {
        Permission permission = permissionService.getPermission(id);
        return ResponseEntity.ok(new DetailedPermissionDTO(permission));
    }

}
