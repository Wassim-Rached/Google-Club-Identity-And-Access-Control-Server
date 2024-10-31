package com.ics.services;

import com.ics.entities.Permission;
import com.ics.exceptions.InputValidationException;
import com.ics.interfaces.services.IPermissionService;
import com.ics.repositories.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService{

    private final PermissionRepository permissionRepository;

    @Override
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).SEARCH_PERMISSIONS)")
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
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).CREATE_PERMISSION)")
    public void savePermission(Permission permission) {
        permissionRepository.save(permission);
    }

    @Override
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).DELETE_PERMISSION)")
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
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).VIEW_PERMISSION)")
    public Permission getPermission(UUID id) {
        return permissionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
