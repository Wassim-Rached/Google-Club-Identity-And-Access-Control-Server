package com.ics.services;

import com.ics.dto.accounts.AccountAuthoritiesEditResponse;
import com.ics.dto.accounts.EditAuthoritiesRequest;
import com.ics.dto.accounts.UpdateAccountDTO;
import com.ics.entities.Permission;
import com.ics.entities.Role;
import com.ics.entities.Account;
import com.ics.events.publishers.*;
import com.ics.events.publishers.emails.AccountDeletedEvent;
import com.ics.events.publishers.emails.PasswordHaveBeenResetedEvent;
import com.ics.exceptions.InputValidationException;
import com.ics.interfaces.services.IAccountService;
import com.ics.repositories.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    // my account
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
    public void resetPassword(Account account,String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        var event = new PasswordHaveBeenResetedEvent(this,account.getEmail());
        eventPublisher.publishEvent(event);
    }

    @Override
    public Account getMyAccount() {
        return  (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public void changeMyPassword(String oldPassword, String newPassword) {
        Account account = getMyAccount();
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new InputValidationException("Old password is incorrect");
        }
        // TODO: count tries and lock account if too many tries

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Override
    public void deleteMyAccount(String password) {
        Account account = getMyAccount();

        // Validate password
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new InputValidationException("Password is incorrect");
        }

        // Clear associations
        account.getRoles().clear();
        account.getPermissions().clear();

        // Remove and delete associated tokens
        if (account.getEmailVerificationToken() != null) {
            emailVerificationTokenRepository.delete(account.getEmailVerificationToken());
            account.setEmailVerificationToken(null);
        }

        if (account.getPasswordResetToken() != null) {
            passwordResetTokenRepository.delete(account.getPasswordResetToken());
            account.setPasswordResetToken(null);
        }

        // Delete the account
        accountRepository.delete(account);

        // trigger event
        var event = new AccountDeletedEvent(this,account.getEmail());
        eventPublisher.publishEvent(event);
    }

    @Override
    public Account updateMyAccount(UpdateAccountDTO requestBody) {
        Account account = getMyAccount();
        account.setPhotoUrl(requestBody.getPhotoUrl());
        return accountRepository.save(account);
    }


    // others accounts
    @Override
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).VERIFY_IDENTITY)")
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
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).LOCK_ACCOUNT)")
    public void lockAccount(boolean lock, Account account) {
        account.setIsLocked(lock);
        accountRepository.save(account);
    }

    @Override
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).CHANGE_MEMBERSHIP)")
    public void changeMembership(boolean member, Account account) {account.setIsMember(member);
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
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).SEARCH_ACCOUNTS)")
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
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).VIEW_ACCOUNT)")
    public Account getAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).VIEW_ACCOUNT)")
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("'"+email+"'" + " Does not exist"));
    }

    @Override
    @Transactional
    @PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).EDIT_AUTHORITIES)")
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
