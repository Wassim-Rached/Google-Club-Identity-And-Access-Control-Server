package com.example.usermanagement.dto.permissions;

import com.example.usermanagement.entities.Permission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SimplePermissionDTO {
    private UUID id;
    private String name;
    private String scope;

    public SimplePermissionDTO(Permission permission) {
        this.id = permission.getId();
        this.name = permission.getName();
        this.scope = permission.getScope();
    }

    public static Set<SimplePermissionDTO> fromPermissions(Set<Permission> permissions) {
        return permissions.stream().map(SimplePermissionDTO::new).collect(java.util.stream.Collectors.toSet());
    }
}
