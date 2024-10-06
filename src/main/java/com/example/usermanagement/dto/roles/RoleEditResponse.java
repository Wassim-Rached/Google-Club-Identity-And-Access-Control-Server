package com.example.usermanagement.dto.roles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleEditResponse {
    private RoleActionSummary roles;
    private PermissionActionSummary permissions;

    public RoleEditResponse() {
        roles = new RoleActionSummary();
        permissions = new PermissionActionSummary();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoleActionSummary {
        private List<String> created = new ArrayList<>();
        private List<String> updated = new ArrayList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PermissionActionSummary {
        private List<String> granted = new ArrayList<>();
        private List<String> revoked = new ArrayList<>();
    }

    public void addCreatedRole(String roleName) {
        roles.getCreated().add(roleName);
    }

    public void addUpdatedRole(String roleName) {
        roles.getUpdated().add(roleName);
    }

    public void addGrantedPermission(String permissionName) {
        permissions.getGranted().add(permissionName);
    }

    public void addRevokedPermission(String permissionName) {
        permissions.getRevoked().add(permissionName);
    }

    public void addGrantedPermissions(List<String> permissionNames) {
        permissions.getGranted().addAll(permissionNames);
    }

    public void addRevokedPermissions(List<String> permissionNames) {
        permissions.getRevoked().addAll(permissionNames);
    }
}

