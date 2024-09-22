package com.example.usermanagement.dto.roles;

import com.example.usermanagement.dto.permissions.SimplePermissionDTO;
import com.example.usermanagement.entities.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SimpleRoleDTO {
    private UUID id;
    private String name;
    private String scope;
    private Set<SimplePermissionDTO> permissions;

    public SimpleRoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.scope = role.getScope();
        this.permissions = SimplePermissionDTO.fromPermissions(role.getPermissions());
    }

}
