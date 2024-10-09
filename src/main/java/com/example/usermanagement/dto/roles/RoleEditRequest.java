package com.example.usermanagement.dto.roles;

import com.example.usermanagement.entities.Account;
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
    private AccountChanges accounts;
    private String description;

    public void validate() {
        Role.validatePublicName(publicName);
        this.permissions.validate();
        this.accounts.validate();
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
}
