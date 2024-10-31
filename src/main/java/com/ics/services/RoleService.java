package com.ics.services;

import com.ics.dto.permissions.PermissionImportResponse;
import com.ics.dto.roles.*;
import com.ics.entities.Account;
import com.ics.entities.Permission;
import com.ics.entities.Role;
import com.ics.exceptions.InputValidationException;
import com.ics.interfaces.services.IRoleService;
import com.ics.repositories.AccountRepository;
import com.ics.repositories.PermissionRepository;
import com.ics.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).SEARCH_ROLES)")
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
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).CREATE_ROLE)")
    public Role createRole(CreateRoleDTO requestBody) {
        Role role = requestBody.toEntity(null);
        // get all permissions from the database
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(role.getPermissions().stream().map(Permission::getId).collect(Collectors.toList())));
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).EDIT_AUTHORITIES)")
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

            List<String> accountsToBeGrantedTo = roleChangeRequest.getAccountsToGrant();
            List<String> accountsToBeRevokedFrom = roleChangeRequest.getAccountsToRevoke();

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
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).DELETE_ROLE)")
    public void deleteRole(UUID id) {
        Role role = roleRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if(role.isSpecial()){
            throw new InputValidationException("Cannot delete special roles");
        }

        // we loop through it because the rel is owned by the account
        for (Account account : role.getAccounts()) {
            account.getRoles().remove(role);
        }

        role.getAccounts().clear();
        role.getPermissions().clear();

        roleRepository.delete(role);
    }

    @Override
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).VIEW_ROLE)")
    public Role getRoleById(UUID id) {
        return roleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<RoleImportResponse> importRoles(List<RoleImportRequest> requestBody){
        List<RoleImportResponse> responses = new ArrayList<>();
        for (var roleImportRequest : requestBody) {
            RoleImportResponse response = new RoleImportResponse();
            responses.add(response);

            Role role = roleImportRequest.toEntity(null);
            response.setRolePublicName(role.getPublicName());

            Optional<Role> a = roleRepository.findByPublicName(role.getPublicName());

            if (a.isPresent()) {
                // skip if tje role already exists
                response.setStatus("skipped");
                continue;
            }else{
                response.setStatus("created");
            }

            Set<Permission> permissions = new HashSet<>();

            // import the permissions
            for (Permission permission : role.getPermissions()) {
                Optional<Permission> permissionExists = permissionRepository.findByPublicName(permission.getPublicName());
                PermissionImportResponse permissionImportResponse = new PermissionImportResponse();
                permissionImportResponse.setPermissionPublicName(permission.getPublicName());
                response.getPermissions().add(permissionImportResponse);
                if(permissionExists.isPresent()){
                    permissionImportResponse.setStatus("skipped");
                    permissions.add(permissionExists.get());
                }else{
                    permissionImportResponse.setStatus("created");
                    permissions.add(permission);
                }
                permissionImportResponse.setPermissionPublicName(permission.getPublicName());
            }
            // update the role with the imported permissions
            // so it doesn't throw sql duplicate exception
            role.setPermissions(permissions);

            roleRepository.save(role);
        }
        return responses;
    }

}
