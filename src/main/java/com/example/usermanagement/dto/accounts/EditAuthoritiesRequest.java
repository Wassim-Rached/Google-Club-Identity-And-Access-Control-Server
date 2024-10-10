package com.example.usermanagement.dto.accounts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EditAuthoritiesRequest {
    private RoleChanges roles;
    private PermissionChanges permissions;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoleChanges {
        private List<String> add;
        private List<String> revoke;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PermissionChanges {
        private List<String> add;
        private List<String> revoke;

    }

    public List<String> getRolesToAdd() {
        if (roles == null) return null;
        return roles.getAdd();
    }

    public List<String> getRolesToRevoke() {
        if (roles == null) return null;
        return roles.getRevoke();
    }

    public List<String> getPermissionsToAdd() {
        if (permissions == null) return null;
        return permissions.getAdd();
    }

    public List<String> getPermissionsToRemove() {
        if (permissions == null) return null;
        return permissions.getRevoke();
    }
}