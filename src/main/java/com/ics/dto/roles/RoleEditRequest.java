package com.ics.dto.roles;

import com.ics.entities.Account;
import com.ics.entities.Permission;
import com.ics.entities.Role;
import com.ics.exceptions.InputValidationException;
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
    private AccountChanges accounts;
    private String description;

    public void validate() {
        Role.validatePublicName(publicName);
        this.permissions.validate(publicName);
        this.accounts.validate();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PermissionChanges {
        private List<String> grant;
        private List<String> revoke;

        public void validate(String rolePublicName) {
            if (grant != null) {
                for(String permissionPublicName : grant) {
                    Role.validateCanBeGrantedPermission(rolePublicName, permissionPublicName);
                    Permission.validatePublicName(permissionPublicName);
                    if(Permission.isSpecial(permissionPublicName)) {
                        throw new InputValidationException("Cannot grant special permissions");
                    }
                }
            }
            if (revoke != null) {
                for(String permissionPublicName : revoke) {
                    Role.validateCanBeRevokedPermission(rolePublicName, permissionPublicName);
                    Permission.validatePublicName(permissionPublicName);
                    if(Permission.isSpecial(permissionPublicName)) {
                        throw new InputValidationException("Cannot revoke special permissions");
                    }
                }
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AccountChanges {
        private List<String> grant;
        private List<String> revoke;

        public void validate() {
            if (grant != null) {
                grant.forEach(Account::validateEmail);
            }
            if (revoke != null) {
                revoke.forEach(Account::validateEmail);
            }
        }
    }

    public List<String> getPermissionsToGrant() {
        return permissions.getGrant();
    }

    public List<String> getPermissionsToRevoke() {
        return permissions.getRevoke();
    }

    public List<String> getAccountsToGrant() {
        return accounts.getGrant();
    }

    public List<String> getAccountsToRevoke() {
        return accounts.getRevoke();
    }

    public String getRoleScope() {
        return Role.getScopeFromPublicName(publicName);
    }
}
