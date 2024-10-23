package com.example.usermanagement.services;

import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.exceptions.InputValidationException;
import com.example.usermanagement.interfaces.services.IPermissionService;
import com.example.usermanagement.repositories.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService{

    private final PermissionRepository permissionRepository;

    @Override
    public Page<Permission> SearchAndSortPermissions(String publicName, String sort, int page, int size, String direction) {
        // default values
        sort = sort != null ? sort : "scope";
        publicName = publicName != null ? publicName : "";
        page = Math.max(page, 0);
        size = Math.min(size, 20);
        size = Math.max(size, 1);

        Sort order = Sort.by(sort);
        if (direction.equals("desc")) {
            order = order.descending();
        }else{
            order = order.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, order);
        return permissionRepository.findByPublicName(publicName, pageable);
    }

    @Override
    public void savePermission(Permission permission) {
        permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(UUID permissionId) {
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(EntityNotFoundException::new);

        if(permission.isSpecial()){
            throw new InputValidationException("Cannot delete special permissions");
        }

        for (var role : permission.getRoles()) {
            role.getPermissions().remove(permission);
        }
        for (var account : permission.getAccounts()) {
            account.getPermissions().remove(permission);
        }

        permission.getRoles().clear();
        permission.getAccounts().clear();

        permissionRepository.delete(permission);
    }

    @Override
    public Permission getPermission(UUID id) {
        return permissionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
