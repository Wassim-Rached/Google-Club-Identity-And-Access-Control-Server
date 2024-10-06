package com.example.usermanagement.services;

import com.example.usermanagement.dto.accounts.EditAuthoritiesRequest;
import com.example.usermanagement.dto.accounts.SafeAccountInfo;
import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.entities.Role;
import com.example.usermanagement.entities.Account;
import com.example.usermanagement.interfaces.services.IAccountService;
import com.example.usermanagement.repositories.PermissionRepository;
import com.example.usermanagement.repositories.RoleRepository;
import com.example.usermanagement.repositories.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void encodeAndSaveAccount(Account userAccount) {
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        accountRepository.save(userAccount);
    }

    @Override
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public Account getMyAccount() {
        return  (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    @Transactional
    public void editAuthorities(UUID accountId, EditAuthoritiesRequest requestBody) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(EntityNotFoundException::new);

        // Handle adding roles
        if (requestBody.getRolesToAdd() != null) {
            List<Role> alreadyExistRolesToAdd = roleRepository.findByPublicNames(requestBody.getRolesToAdd());
            account.getRoles().addAll(alreadyExistRolesToAdd);

            // create the roles that are not in the database
            for (String publicRoleName : requestBody.getRolesToAdd()) {
                Role role = new Role(publicRoleName);
                if(alreadyExistRolesToAdd.contains(role)) continue;
                account.getRoles().add(role);
            }
        }

        // Handle removing roles
        if (requestBody.getRolesToRevoke() != null) {
            Set<Role> accountRoles = account.getRoles();
            accountRoles.removeIf(role -> requestBody.getRolesToRevoke().contains(role.getPublicName()));
        }

        // Handle adding permissions
        if (requestBody.getPermissionsToAdd() != null) {
            List<Permission> alreadyExistPermissionsToAdd = permissionRepository.findByPublicNames(requestBody.getPermissionsToAdd());
            account.getPermissions().addAll(alreadyExistPermissionsToAdd);

            // create the permissions that are not in the database
            for (String publicPermissionName : requestBody.getPermissionsToAdd()) {
                Permission permission = new Permission(publicPermissionName);
                if(alreadyExistPermissionsToAdd.contains(permission)) continue;
                account.getPermissions().add(permission);
            }

        }

        // Handle removing permissions
        if (requestBody.getPermissionsToRemove() != null) {
            Set<Permission> accountPermissions = account.getPermissions();
            accountPermissions.removeIf(permission -> requestBody.getPermissionsToRemove().contains(permission.getPublicName()));
        }

        accountRepository.save(account);
    }
}
