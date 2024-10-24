package com.example.usermanagement.dto.accounts;

import com.example.usermanagement.dto.permissions.GeneralPermissionDTO;
import com.example.usermanagement.dto.roles.GeneralRoleDTO;
import com.example.usermanagement.entities.Account;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class DetailedAccountDTO {
    private String email;
    private String photoUrl;
    private Boolean isEmailVerified;
    private Boolean isLocked;
    private Boolean isIdentityVerified;
    private Boolean isMember;
    private Instant createdAt;
    private Set<GeneralRoleDTO> roles;
    private Set<GeneralPermissionDTO> permissions;


    public DetailedAccountDTO(Account account) {
        this.email = account.getEmail();
        this.photoUrl = account.getPhotoUrl();
        this.isEmailVerified = account.getIsEmailVerified();
        this.isLocked = account.getIsLocked();
        this.isIdentityVerified = account.getIsIdentityVerified();
        this.createdAt = account.getCreatedAt();
        this.isMember = account.getIsMember();
        this.roles = GeneralRoleDTO.fromRoles(account.getRoles());
        this.permissions = GeneralPermissionDTO.fromPermissions(account.getPermissions());
    }
}
