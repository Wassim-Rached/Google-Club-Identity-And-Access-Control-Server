package com.example.usermanagement.services;

import com.example.usermanagement.dto.permissions.SimplePermissionDTO;
import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.interfaces.services.IPermissionService;
import com.example.usermanagement.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService{

    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public void savePermission(Permission permission) {
        permissionRepository.save(permission);
    }
}
