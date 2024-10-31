package com.ics.dto.roles;

import com.ics.entities.Permission;
import com.ics.entities.Role;
import com.ics.exceptions.InputValidationException;
import com.ics.interfaces.dto.IEntityDTO;
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
        Role.validateCanBeCreated(name, scope);

        Role role = new Role();
        role.setName(name);
        role.setScope(scope);
        role.setDescription(description);
        role.setPermissions(permissions);
        return role;
    }

}
