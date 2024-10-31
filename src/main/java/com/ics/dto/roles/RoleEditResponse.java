package com.ics.dto.roles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleEditResponse {
    private String rolePublicName;
    private PermissionActionSummary permissions;
    private AccountActionSummary accounts;

    public RoleEditResponse() {
        permissions = new PermissionActionSummary();
        accounts = new AccountActionSummary();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PermissionActionSummary {
        private List<String> granted = new ArrayList<>();
        private List<String> revoked = new ArrayList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AccountActionSummary {
        private List<String> granted = new ArrayList<>();
        private List<String> revoked = new ArrayList<>();
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

    public void addGrantedAccount(String accountEmail) {
        accounts.getGranted().add(accountEmail);
    }

    public void addRevokedAccount(String accountEmail) {
        accounts.getRevoked().add(accountEmail);
    }

    public void addGrantedAccounts(List<String> accountEmails) {
        accounts.getGranted().addAll(accountEmails);
    }

    public void addRevokedAccounts(List<String> accountEmails) {
        accounts.getRevoked().addAll(accountEmails);
    }
}

