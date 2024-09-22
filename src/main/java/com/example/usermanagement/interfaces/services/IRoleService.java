package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.entities.Account;
import com.example.usermanagement.entities.Role;

import java.util.List;
import java.util.UUID;

/*
    Notes:
     - DTO classes should not be returned from service methods.
*/
public interface IRoleService {
    List<Role> getAllRoles();
    void saveRole(Role role);
    void addPermissionToRole(UUID roleId, UUID permissionId);
}
