package com.example.usermanagement.services;

import com.example.usermanagement.dto.roles.CreateRoleDTO;
import com.example.usermanagement.dto.roles.RoleEditRequest;
import com.example.usermanagement.dto.roles.RoleEditResponse;
import com.example.usermanagement.entities.Account;
import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.entities.Role;
import com.example.usermanagement.interfaces.services.IRoleService;
import com.example.usermanagement.repositories.AccountRepository;
import com.example.usermanagement.repositories.PermissionRepository;
import com.example.usermanagement.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final AccountRepository accountRepository;

    @Override
    public Page<Role> searchAndSortRoles(String publicName, String sort, int page, int size, String direction) {
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
        return roleRepository.findByPublicName(publicName, pageable);
    }

    @Override
    @Transactional
    public Role createRole(CreateRoleDTO requestBody) {
        Role role = requestBody.toEntity(null);
        // get all permissions from the database
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(role.getPermissions().stream().map(Permission::getId).collect(Collectors.toList())));
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public List<RoleEditResponse> editRoles(List<RoleEditRequest> requestBody) {
        List<RoleEditResponse> responses = new ArrayList<>();
        requestBody.forEach(RoleEditRequest::validate);

        Set<Role> roles = roleRepository.findByPublicNames(requestBody.stream().map(RoleEditRequest::getPublicName).toList());

        for (RoleEditRequest roleChangeRequest : requestBody) {
            RoleEditResponse response = new RoleEditResponse();
            response.setRolePublicName(roleChangeRequest.getPublicName());

            Role role = roles.stream().filter(r -> r.getPublicName().equals(roleChangeRequest.getPublicName())).findFirst().orElseThrow(() -> new EntityNotFoundException("Role not found : " + roleChangeRequest.getPublicName()));

            List<String> permissionsToGrant = roleChangeRequest.getPermissionsToGrant();
            List<String> permissionsToRevoke = roleChangeRequest.getPermissionsToRevoke();

            List<String> accountsToBeGrantedTo = roleChangeRequest.getAccounts().getGrant();
            List<String> accountsToBeRevokedFrom = roleChangeRequest.getAccounts().getRevoke();

            if (permissionsToGrant != null) {
                Set<Permission> alreadyExistsPermissions = permissionRepository.findByPublicNames(permissionsToGrant);

                // Add permissions to role
                role.getPermissions().addAll(alreadyExistsPermissions);
                response.addGrantedPermissions(alreadyExistsPermissions.stream().map(Permission::getPublicName).toList());

                // create and add the non-already-existing permissions
                for (String publicPermissionName : permissionsToGrant) {
                    Permission permission = new Permission(publicPermissionName);
                    if (alreadyExistsPermissions.contains(permission)) continue;
                    throw new EntityNotFoundException("Permission not found : " + publicPermissionName);
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

            if(accountsToBeGrantedTo != null){
                List<Account> accounts = accountRepository.findByEmails(accountsToBeGrantedTo);
                for (Account account : accounts) {
                    account.getRoles().add(role);
                    response.addGrantedAccount(account.getEmail());
                }
            }

            if(accountsToBeRevokedFrom != null){
                for(Account account : role.getAccounts()){
                    if(accountsToBeRevokedFrom.contains(account.getEmail())) {
                        account.getRoles().remove(role);
                        response.addRevokedAccount(account.getEmail());
                    }
                }
            }

            accountRepository.saveAll(role.getAccounts());
            responses.add(response);
        }

        roleRepository.saveAll(roles);
        return responses;
    }

    @Override
    public void deleteRole(UUID id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role getRoleById(UUID id) {
        return roleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
