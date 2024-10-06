package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.entities.Permission;

import java.util.List;
import java.util.UUID;

/*
    Notes:
     - DTO classes should not be returned from service methods.
*/
public interface IPermissionService {
    List<Permission> getAllPermissions();
    void savePermission(Permission permission);
    void deletePermission(UUID permissionId);
    Permission getPermission(UUID id);
}
