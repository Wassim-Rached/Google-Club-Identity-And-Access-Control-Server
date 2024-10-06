package com.example.usermanagement.dto.roles;

import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoleEditRequest {
    private String publicName;
    private PermissionChanges permissions;

    public void validate() {
        Role.validatePublicName(publicName);
        this.permissions.validate();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PermissionChanges {
        private List<String> grant;
        private List<String> revoke;

        public void validate() {
            if (grant != null) {
                grant.forEach(Permission::validatePublicName);
            }
            if (revoke != null) {
                revoke.forEach(Permission::validatePublicName);
            }
        }

    }

    public List<String> getPermissionsToGrant() {
        return permissions.getGrant();
    }

    public List<String> getPermissionsToRevoke() {
        return permissions.getRevoke();
    }
}
