package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.entities.Permission;

import java.util.List;

/*
    Notes:
     - DTO classes should not be returned from service methods.
*/
public interface IPermissionService {
    List<Permission> getAllPermissions();
    void savePermission(Permission permission);
}
