package com.example.usermanagement.dto.roles;

import com.example.usermanagement.entities.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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

    public GeneralRoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.scope = role.getScope();
        this.description = role.getDescription();
    }

    public static Set<GeneralRoleDTO> fromRoles(Set<Role> roles) {
        return roles.stream().map(GeneralRoleDTO::new).collect(Collectors.toSet());
    }
}
