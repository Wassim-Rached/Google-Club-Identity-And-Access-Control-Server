package com.ics.dto.permissions;

import com.ics.dto.accounts.GeneralAccountDTO;
import com.ics.dto.roles.GeneralRoleDTO;
import com.ics.entities.Permission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DetailedPermissionDTO {
    private UUID id;
    private String name;
    private String scope;
    private String description;
    private List<GeneralAccountDTO> accounts;
    private Set<GeneralRoleDTO> roles;

    public DetailedPermissionDTO(Permission permission) {
        this.id = permission.getId();
        this.name = permission.getName();
        this.scope = permission.getScope();
        this.description = permission.getDescription();
        this.accounts = GeneralAccountDTO.fromAccounts(permission.getAccounts());
        this.roles = GeneralRoleDTO.fromRoles(permission.getRoles());
    }

    public static Set<GeneralPermissionDTO> fromPermissions(Set<Permission> permissions) {
        return permissions.stream().map(GeneralPermissionDTO::new).collect(java.util.stream.Collectors.toSet());
    }
}
