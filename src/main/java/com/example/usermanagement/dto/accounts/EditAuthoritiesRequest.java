package com.example.usermanagement.dto.accounts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class EditAuthoritiesRequest {
    private String email;
    private RoleChanges roles;
    private PermissionChanges permissions;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoleChanges {
        private Set<String> grant;
        private Set<String> revoke;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PermissionChanges {
        private Set<String> grant;
        private Set<String> revoke;

    }

    public Set<String> getRolesTogrant() {
        if (roles == null) return null;
        return roles.getGrant();
    }

    public Set<String> getRolesToRevoke() {
        if (roles == null) return null;
        return roles.getRevoke();
    }

    public Set<String> getPermissionsToGrant() {
        if (permissions == null) return null;
        return permissions.getGrant();
    }

    public Set<String> getPermissionsToRevoke() {
        if (permissions == null) return null;
        return permissions.getRevoke();
    }
}