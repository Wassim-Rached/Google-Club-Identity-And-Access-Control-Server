package com.example.usermanagement.services;

import com.example.usermanagement.dto.accounts.AccountAuthoritiesEditResponse;
import com.example.usermanagement.dto.accounts.EditAuthoritiesRequest;
import com.example.usermanagement.dto.accounts.UpdateAccountDTO;
import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.entities.Role;
import com.example.usermanagement.entities.Account;
import com.example.usermanagement.events.publishers.*;
import com.example.usermanagement.exceptions.ForbiddenException;
import com.example.usermanagement.interfaces.services.IAccountService;
import com.example.usermanagement.repositories.PermissionRepository;
import com.example.usermanagement.repositories.RoleRepository;
import com.example.usermanagement.repositories.AccountRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void encodeAndSaveAccount(Account userAccount) {
        Account alreadyExists = accountRepository.findByEmail(userAccount.getEmail()).orElse(null);
        if (alreadyExists != null) {
            throw new EntityExistsException("Account with this email already exists");
        }

        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        accountRepository.save(userAccount);
    }

    @Override
    public void verifyAccountEmail(String accountEmail) {
        Account account = accountRepository.findByEmail(accountEmail).orElseThrow(
                ()-> new EntityNotFoundException("Account with email " + accountEmail + " not found")
        );

        if(account.getIsEmailVerified()) return;

        account.setIsEmailVerified(true);
        accountRepository.save(account);

        // Publish event
        var event = new AccountEmailVerifiedEvent(this,account.getEmail());
        eventPublisher.publishEvent(event);
    }

    @Override
    public void requestResetPassword(Account account) {
        // TODO: generate,persist and send reset password token
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // TODO: validate token and change password
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void verifyIdentity(boolean isVerified,Account account) {
        account.setIsIdentityVerified(isVerified);
        accountRepository.save(account);

        // Publish event
        if(isVerified){
            var event = new AccountIdentityVerifiedEvent(this,account.getEmail());
            eventPublisher.publishEvent(event);
        }else{
            var event = new AccountIdentityUnverifiedEvent(this,account.getEmail());
            eventPublisher.publishEvent(event);
        }
    }

    @Override
    public void lockAccount(boolean lock, Account account) {
        account.setIsLocked(lock);
        accountRepository.save(account);
    }

    @Override
    public void changeMembership(boolean member, Account account) {
        account.setIsIdentityVerified(member);
        accountRepository.save(account);

        // Publish event
        if(member){
            var event = new AccountBecomeMemberEvent(this,account.getEmail());
            eventPublisher.publishEvent(event);
        }else{
            var event = new AccountNoLongerMemberEvent(this,account.getEmail());
            eventPublisher.publishEvent(event);
        }
    }

    @Override
    public void changeMyPassword(String oldPassword, String newPassword) {
        Account account = getMyAccount();
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new ForbiddenException("Old password is incorrect");
        }
        // TODO: count tries and lock account if too many tries

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
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
    public Account updateMyAccount(UpdateAccountDTO requestBody) {
        Account account = getMyAccount();
        account.setPhotoUrl(requestBody.getPhotoUrl());
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("'"+email+"'" + " Does not exist"));
    }

    @Override
    @Transactional
    public List<AccountAuthoritiesEditResponse> editAuthorities(List<EditAuthoritiesRequest> requestBody) {
        List<AccountAuthoritiesEditResponse> responses = new ArrayList<>();

        for (EditAuthoritiesRequest accountChangeRequest : requestBody) {
            accountChangeRequest.validate();

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
