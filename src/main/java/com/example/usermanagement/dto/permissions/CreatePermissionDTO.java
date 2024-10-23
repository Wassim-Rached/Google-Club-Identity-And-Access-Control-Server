package com.example.usermanagement.dto.permissions;

import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.exceptions.InputValidationException;
import com.example.usermanagement.interfaces.dto.IEntityDTO;
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
        if(name == null || name.isBlank())
            throw new InputValidationException("Permission name is required");
        if(scope == null || scope.isBlank())
            throw new InputValidationException("Permission scope is required");
        if(!name.matches("[a-zA-Z_]+"))
            throw new InputValidationException("Permission name can only contain letters and _ : " + name);
        if(!scope.matches("[a-zA-Z_]+"))
            throw new InputValidationException("Permission scope can only contain letters and _ : " + scope);
        if(scope.equals("special")){
            throw new InputValidationException("Permission scope cannot be 'special'");
        }

        Permission permission = new Permission();
        permission.setName(name);
        permission.setScope(scope);
        permission.setDescription(description);
        return permission;
    }
}
