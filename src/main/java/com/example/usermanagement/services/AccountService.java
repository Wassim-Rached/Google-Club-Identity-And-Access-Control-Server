package com.example.usermanagement.services;

import com.example.usermanagement.dto.accounts.EditAuthoritiesRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Account> searchAndSortAccounts(String email, String sort, int page, int size, String direction){
        // default values
        sort = sort != null ? sort : "email";
        email = email != null ? email : "";
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
        return accountRepository.findByEmail(email, pageable);
    }

    @Override
    public Account getMyAccount() {
        return  (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public Account getAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void editAuthorities(UUID accountId, EditAuthoritiesRequest requestBody) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println(requestBody.getRolesToAdd());
        // Handle adding roles
        if (requestBody.getRolesToAdd() != null) {
            List<Role> alreadyExistRolesToAdd = roleRepository.findByPublicNames(requestBody.getRolesToAdd());
            account.getRoles().addAll(alreadyExistRolesToAdd);

            // create the roles that are not in the database
            for (String publicRoleName : requestBody.getRolesToAdd()) {
                Role role = new Role(publicRoleName);
                System.out.println("Role: " + role);
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

        System.out.println(account.getRoles());
        System.out.println(account.getPermissions());

        accountRepository.save(account);
    }
}
