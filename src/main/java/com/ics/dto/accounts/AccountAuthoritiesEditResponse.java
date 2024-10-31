package com.ics.dto.accounts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AccountAuthoritiesEditResponse {
    private String email;
    private RoleActionSummary roles;
    private PermissionActionSummary permissions;

    public AccountAuthoritiesEditResponse() {
        roles = new RoleActionSummary();
        permissions = new PermissionActionSummary();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoleActionSummary {
        private List<String> granted = new ArrayList<>();
        private List<String> revoked = new ArrayList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PermissionActionSummary {
        private List<String> granted = new ArrayList<>();
        private List<String> revoked = new ArrayList<>();
    }

    public void addGrantedRole(String roleName) {
        roles.getGranted().add(roleName);
    }


    public void addRevokedRole(String roleName) {
        roles.getRevoked().add(roleName);
    }

    public void addGrantedRoles(List<String> roleNames) {
        roles.getGranted().addAll(roleNames);
    }

    public void addRevokedRoles(List<String> roleNames) {
        roles.getRevoked().addAll(roleNames);
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