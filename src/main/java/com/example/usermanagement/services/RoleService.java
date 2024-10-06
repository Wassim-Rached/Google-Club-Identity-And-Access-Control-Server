package com.example.usermanagement.services;

import com.example.usermanagement.dto.roles.RoleEditRequest;
import com.example.usermanagement.dto.roles.RoleEditResponse;
import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.entities.Role;
import com.example.usermanagement.interfaces.services.IRoleService;
import com.example.usermanagement.repositories.PermissionRepository;
import com.example.usermanagement.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public RoleEditResponse editRoles(List<RoleEditRequest> requestBody) {
        RoleEditResponse response = new RoleEditResponse();
        requestBody.forEach(RoleEditRequest::validate);

        List<Role> roles = roleRepository.findByPublicNames(requestBody.stream().map(RoleEditRequest::getPublicName).toList());

        // create and add the non-already-existing roles
        for (RoleEditRequest roleChangeRequest : requestBody) {
            Role role = new Role(roleChangeRequest.getPublicName());
            if(roles.contains(role)){
                response.addUpdatedRole(role.getPublicName());
                continue;
            }
            roles.add(role);
            response.addCreatedRole(role.getPublicName());
        }

        for (RoleEditRequest roleChangeRequest : requestBody) {
            Role role = roles.stream().filter(r -> r.getPublicName().equals(roleChangeRequest.getPublicName())).findFirst().orElseThrow();

            List<String> permissionsToGrant = roleChangeRequest.getPermissionsToGrant();
            List<String> permissionsToRevoke = roleChangeRequest.getPermissionsToRevoke();

            if (permissionsToGrant != null) {
                List<Permission> alreadyExistsPermissions = permissionRepository.findByPublicNames(permissionsToGrant);

                // Add permissions to role
                role.getPermissions().addAll(alreadyExistsPermissions);
                response.addGrantedPermissions(alreadyExistsPermissions.stream().map(Permission::getPublicName).toList());

                // create and add the non-already-existing permissions
                for (String publicPermissionName : permissionsToGrant) {
                    Permission permission = new Permission(publicPermissionName);
                    if (alreadyExistsPermissions.contains(permission)) continue;
                    role.getPermissions().add(permission);
                    response.addGrantedPermission(permission.getPublicName());
                }
            }

            // Remove permissions from the role
            if (permissionsToRevoke != null) {
                role.getPermissions().removeIf(permission -> {
                    var shouldBeRevoked =  permissionsToRevoke.contains(permission.getPublicName());
                    if (shouldBeRevoked) response.addRevokedPermission(permission.getPublicName());
                    return shouldBeRevoked;
                });
                response.addRevokedPermissions(permissionsToRevoke);
            }
        }

        roleRepository.saveAll(roles);
        return response;
    }

    @Override
    public void deleteRole(UUID id) {
        roleRepository.deleteById(id);
    }
}
