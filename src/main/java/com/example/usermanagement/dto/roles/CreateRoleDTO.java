package com.example.usermanagement.dto.roles;

import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.entities.Role;
import com.example.usermanagement.exceptions.InputValidationException;
import com.example.usermanagement.interfaces.dto.IEntityDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateRoleDTO implements IEntityDTO<Role,Void> {
    private String name;
    private String scope;
    private String description;
    private Set<Permission> permissions;

    @Override
    public Role toEntity(Void aVoid) {
        if(name == null || name.isBlank())
            throw new InputValidationException("Role name is required");
        if(scope == null || scope.isBlank())
            throw new InputValidationException("Role scope is required");
        if(!name.matches("[a-zA-Z_]+"))
            throw new InputValidationException("Role name can only contain letters and _ : " + name);
        if(!scope.matches("[a-zA-Z_]+"))
            throw new InputValidationException("Role scope can only contain letters and _ : " + scope);
        if(scope.equals("special")){
            throw new InputValidationException("Role scope cannot be 'special'");
        }
        Role role = new Role();
        role.setName(name);
        role.setScope(scope);
        role.setDescription(description);
        role.setPermissions(permissions);
        return role;
    }

}
