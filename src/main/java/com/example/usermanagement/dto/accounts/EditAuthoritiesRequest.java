package com.example.usermanagement.dto.accounts;

import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.exceptions.InputValidationException;
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

    public void validate() {
        if (email == null || email.isBlank()) {
            throw new InputValidationException("Email is required");
        }
        if(this.permissions != null){
            this.permissions.validate();
        }
    }

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

        public void validate() {
            if (this.grant != null) {
                for (String permissionPublicName : grant){
                    if (Permission.isSpecial(permissionPublicName))
                        throw new InputValidationException("Cannot grant special permissions");
                }
            }

            if (this.revoke != null) {
                for (String permissionPublicName : revoke) {
                    if (Permission.isSpecial(permissionPublicName))
                        throw new InputValidationException("Cannot revoke special permissions");
                }
            }
        }
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