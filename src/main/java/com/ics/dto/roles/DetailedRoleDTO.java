package com.ics.dto.roles;

import com.ics.dto.accounts.GeneralAccountDTO;
import com.ics.dto.permissions.GeneralPermissionDTO;
import com.ics.entities.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DetailedRoleDTO {
    private UUID id;
    private String name;
    private String scope;
    private String description;
    private Set<GeneralPermissionDTO> permissions;
    private List<GeneralAccountDTO> accounts;

    public DetailedRoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.scope = role.getScope();
        this.description = role.getDescription();
        this.permissions = GeneralPermissionDTO.fromPermissions(role.getPermissions());
        this.accounts = GeneralAccountDTO.fromAccounts(role.getAccounts());
    }

}
