package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.dto.roles.RoleEditRequest;
import com.example.usermanagement.dto.roles.RoleEditResponse;
import com.example.usermanagement.entities.Role;

import java.util.List;
import java.util.UUID;

/*
    Notes:
     - DTO classes should not be returned from service methods.
*/
public interface IRoleService {
    List<Role> getAllRoles();
    RoleEditResponse editRoles(List<RoleEditRequest> requestBody);
    void deleteRole(UUID id);
}
