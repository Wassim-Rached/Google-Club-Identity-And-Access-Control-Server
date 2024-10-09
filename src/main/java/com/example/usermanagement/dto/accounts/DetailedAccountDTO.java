package com.example.usermanagement.dto.accounts;

import com.example.usermanagement.dto.permissions.GeneralPermissionDTO;
import com.example.usermanagement.dto.roles.GeneralRoleDTO;
import com.example.usermanagement.entities.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class DetailedAccountDTO {
    private String email;
    private String photoUrl;
    private Set<GeneralRoleDTO> roles;
    private Set<GeneralPermissionDTO> permissions;

    public DetailedAccountDTO(Account account) {
        this.email = account.getEmail();
        this.photoUrl = account.getPhotoUrl();
        this.roles = GeneralRoleDTO.fromRoles(account.getRoles());
        this.permissions = GeneralPermissionDTO.fromPermissions(account.getPermissions());
    }
}
