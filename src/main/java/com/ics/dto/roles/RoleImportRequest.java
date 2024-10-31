package com.ics.dto.roles;

import com.ics.dto.permissions.PermissionImportRequest;
import com.ics.entities.Role;
import com.ics.interfaces.dto.IEntityDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class RoleImportRequest implements IEntityDTO<Role,Void> {
    private String name;
    private String scope;
    private String description;
    private Set<PermissionImportRequest> permissions = Set.of();

    @Override
    public Role toEntity(Void aVoid) {
        Role.validateCanBeCreated(name, scope);

        Role role = new Role();
        role.setName(name);
        role.setScope(scope);
        role.setDescription(description);
        role.setPermissions(permissions.stream().map(permissionImportRequest -> permissionImportRequest.toEntity(null)).collect(Collectors.toSet()));

        return role;
    }
}
