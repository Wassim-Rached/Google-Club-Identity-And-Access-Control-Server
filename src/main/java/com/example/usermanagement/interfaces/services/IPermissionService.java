package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.entities.Permission;
import org.springframework.data.domain.Page;

import java.util.UUID;

/*
    Notes:
     - DTO classes should not be returned from service methods.
*/
public interface IPermissionService {
    Page<Permission> SearchAndSortPermissions(String publicName, String sort, int page, int size, String direction);
    void savePermission(Permission permission);
    void deletePermission(UUID permissionId);
    Permission getPermission(UUID id);
}
