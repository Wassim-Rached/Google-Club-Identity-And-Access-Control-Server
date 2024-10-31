package com.ics.controllers;

import com.ics.dto.roles.*;
import com.ics.entities.Role;
import com.ics.interfaces.services.IRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        public ResponseEntity<Page<GeneralRoleDTO>> searchAndSortRoles(
                @RequestParam(required = false) String publicName,
                @RequestParam(required = false) String sort,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "asc") String direction
        ) {
            Page<Role> roles = roleService.searchAndSortRoles(publicName,sort,page,size,direction);
            Page<GeneralRoleDTO> rolesDTO = roles.map(GeneralRoleDTO::new);
            return ResponseEntity.ok(rolesDTO);
        }

        @PostMapping("/import")
        @Transactional
        public ResponseEntity<List<RoleImportResponse>> importRoles(@RequestBody List<RoleImportRequest> requestBody) {
            List<RoleImportResponse> responses = roleService.importRoles(requestBody);
            return ResponseEntity.ok(responses);
        }

        @PostMapping
        public ResponseEntity<UUID> createRole(@RequestBody CreateRoleDTO requestBody) {
            Role role = roleService.createRole(requestBody);
            return ResponseEntity.ok(role.getId());
        }

        @PostMapping("/edit")
        public ResponseEntity<List<RoleEditResponse>> editRoles(@RequestBody List<RoleEditRequest> requestBody) {
            return ResponseEntity.ok(roleService.editRoles(requestBody));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
            roleService.deleteRole(id);
            return ResponseEntity.ok().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<DetailedRoleDTO> getRole(@PathVariable UUID id) {
            Role role = roleService.getRoleById(id);
            return ResponseEntity.ok(new DetailedRoleDTO(role));
        }
}
