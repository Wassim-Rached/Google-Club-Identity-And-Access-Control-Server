package com.ics.dto.permissions;

import com.ics.entities.Permission;
import com.ics.exceptions.InputValidationException;
import com.ics.interfaces.dto.IEntityDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePermissionDTO implements IEntityDTO<Permission,Void> {
    private String name;
    private String scope;
    private String description;

    @Override
    public Permission toEntity(Void aVoid) {
        Permission.validateCanBeCreated(name, scope);

        Permission permission = new Permission();
        permission.setName(name);
        permission.setScope(scope);
        permission.setDescription(description);
        return permission;
    }
}
