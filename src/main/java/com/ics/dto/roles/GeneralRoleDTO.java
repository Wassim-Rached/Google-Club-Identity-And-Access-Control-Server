package com.ics.dto.roles;

import com.ics.dto.permissions.GeneralPermissionDTO;
import com.ics.entities.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class GeneralRoleDTO {
    private UUID id;
    private String name;
    private String scope;
    private String description;
    private Set<GeneralPermissionDTO> permissions;

    public GeneralRoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.scope = role.getScope();
        this.description = role.getDescription();
        this.permissions = GeneralPermissionDTO.fromPermissions(role.getPermissions());
    }

    public static Set<GeneralRoleDTO> fromRoles(Set<Role> roles) {
        return roles.stream().map(GeneralRoleDTO::new).collect(Collectors.toSet());
    }
}
