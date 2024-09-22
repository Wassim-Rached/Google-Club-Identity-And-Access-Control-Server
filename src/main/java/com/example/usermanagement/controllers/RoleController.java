package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.StandardApiResponse;
import com.example.usermanagement.dto.roles.SimpleRoleDTO;
import com.example.usermanagement.entities.Role;
import com.example.usermanagement.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

        private final RoleService roleService;

        @GetMapping
        public ResponseEntity<StandardApiResponse<List<SimpleRoleDTO>>> getAllRoles() {
            var roles = roleService.getAllRoles().stream().map(SimpleRoleDTO::new).toList();
            return ResponseEntity.ok(new StandardApiResponse<>(roles));
        }

        @PostMapping
        public ResponseEntity<StandardApiResponse<UUID>> createRole(@RequestBody Role role) {
            roleService.saveRole(role);
            return ResponseEntity.ok(new StandardApiResponse<>(role.getId()));
        }

        @PostMapping("/{roleId}/permissions/{permissionId}")
        public ResponseEntity<StandardApiResponse<Void>> addPermissionToRole(@PathVariable UUID roleId, @PathVariable UUID permissionId) {
            roleService.addPermissionToRole(roleId, permissionId);
            return ResponseEntity.ok(new StandardApiResponse<>());
        }
}
