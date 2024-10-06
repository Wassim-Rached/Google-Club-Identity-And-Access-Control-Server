package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.roles.RoleEditRequest;
import com.example.usermanagement.dto.roles.RoleEditResponse;
import com.example.usermanagement.dto.roles.SimpleRoleDTO;
import com.example.usermanagement.interfaces.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

        private final IRoleService roleService;

        @GetMapping
        public ResponseEntity<List<SimpleRoleDTO>> getAllRoles() {
            var roles = roleService.getAllRoles().stream().map(SimpleRoleDTO::new).toList();
            return ResponseEntity.ok(roles);
        }

        @PostMapping("/edit")
        public ResponseEntity<RoleEditResponse> editRoles(@RequestBody List<RoleEditRequest> requestBody) {
            var res = roleService.editRoles(requestBody);
            return ResponseEntity.ok(res);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
            roleService.deleteRole(id);
            return ResponseEntity.ok().build();
        }
}
