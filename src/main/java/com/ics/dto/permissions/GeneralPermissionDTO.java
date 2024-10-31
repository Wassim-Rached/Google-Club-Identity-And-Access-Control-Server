package com.ics.dto.permissions;

import com.ics.entities.Permission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GeneralPermissionDTO {
    private UUID id;
    private String name;
    private String scope;
    private String description;

    public GeneralPermissionDTO(Permission permission) {
        this.id = permission.getId();
        this.name = permission.getName();
        this.scope = permission.getScope();
        this.description = permission.getDescription();
    }

    public static Set<GeneralPermissionDTO> fromPermissions(Set<Permission> permissions) {
        return permissions.stream().map(GeneralPermissionDTO::new).collect(java.util.stream.Collectors.toSet());
    }
}
