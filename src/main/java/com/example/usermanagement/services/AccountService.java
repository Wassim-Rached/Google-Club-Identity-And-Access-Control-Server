package com.example.usermanagement.services;

import com.example.usermanagement.dto.accounts.AccountAuthoritiesEditResponse;
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

import java.util.ArrayList;
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
    public List<AccountAuthoritiesEditResponse> editAuthorities(List<EditAuthoritiesRequest> requestBody) {
        List<AccountAuthoritiesEditResponse> responses = new ArrayList<>();

        for (EditAuthoritiesRequest accountChangeRequest : requestBody) {
            AccountAuthoritiesEditResponse response = new AccountAuthoritiesEditResponse();
            response.setEmail(accountChangeRequest.getEmail());

            Account account = accountRepository.findByEmail(accountChangeRequest.getEmail())
                    .orElseThrow(EntityNotFoundException::new);

            // Names
            Set<String> rolesToGrantNames = accountChangeRequest.getRolesTogrant();
            Set<String> rolesToRemoveNames = accountChangeRequest.getRolesToRevoke();
            Set<String> permissionsToGrantNames = accountChangeRequest.getPermissionsToGrant();
            Set<String> permissionsToRevokeNames = accountChangeRequest.getPermissionsToRevoke();

            // Entities
            Set<Role> rolesToGrant = roleRepository.findByPublicNames(rolesToGrantNames);
            Set<Role> rolesToRemove = roleRepository.findByPublicNames(rolesToRemoveNames);
            Set<Permission> permissionsToGrant = permissionRepository.findByPublicNames(permissionsToGrantNames);
            Set<Permission> permissionsToRevoke = permissionRepository.findByPublicNames(permissionsToRevokeNames);

            // Handle adding roles
            account.getRoles().addAll(rolesToGrant);

            // Handle removing roles
            account.getRoles().removeAll(rolesToRemove);

            // Handle adding permissions
            account.getPermissions().addAll(permissionsToGrant);

            // Handle removing permissions
            account.getPermissions().removeAll(permissionsToRevoke);

            accountRepository.save(account);
            responses.add(response);
        }

        return responses;
    }
}
