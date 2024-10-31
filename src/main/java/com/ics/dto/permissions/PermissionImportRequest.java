package com.ics.dto.permissions;

import com.ics.entities.Permission;
import com.ics.interfaces.dto.IEntityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionImportRequest implements IEntityDTO<Permission,Void> {
    private String name;
    private String description;
    private String scope;

    @Override
    public Permission toEntity(Void aVoid) {
        Permission.validateCanBeCreated(name, scope);

        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(description);
        permission.setScope(scope);
        return permission;
    }
}
