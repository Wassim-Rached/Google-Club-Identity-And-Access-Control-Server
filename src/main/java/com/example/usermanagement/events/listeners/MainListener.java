package com.example.usermanagement.events.listeners;

import com.example.usermanagement.entities.Account;
import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.events.publishers.*;
import com.example.usermanagement.exceptions.IrregularBehaviourException;
import com.example.usermanagement.repositories.AccountRepository;
import com.example.usermanagement.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainListener {

    private final AccountRepository accountRepository;
    private final PermissionRepository permissionRepository;

    @EventListener(AccountEmailVerifiedEvent.class)
    public void onAccountEmailVerifiedEvent(AccountEmailVerifiedEvent event) {
        // this method is mainly for granting the permissions related to
        // having a verified email to the account
        String permissionPublicName = "special.perm.verified_email";
        Account account = accountRepository.findByEmail(event.getEmail()).orElseThrow(() -> new IrregularBehaviourException("Account not found"));
        Permission permission = permissionRepository.findByPublicName(permissionPublicName).orElseThrow(() -> new IrregularBehaviourException("Permission not found"));

        account.getPermissions().add(permission);
        accountRepository.save(account);
    }

    @EventListener(AccountBecomeMemberEvent.class)
    public void onAccountBecomeMemberEvent(AccountBecomeMemberEvent event) {
        // this method is mainly for granting the permissions related to
        // being a member to the account
        String permissionPublicName = "special.perm.membership";
        Account account = accountRepository.findByEmail(event.getEmail()).orElseThrow(() -> new IrregularBehaviourException("Account not found"));
        Permission permission = permissionRepository.findByPublicName(permissionPublicName).orElseThrow(() -> new IrregularBehaviourException("Permission not found"));

        account.getPermissions().add(permission);
        accountRepository.save(account);
    }

    @EventListener(AccountNoLongerMemberEvent.class)
    public void onAccountNoLongerMemberEvent(AccountNoLongerMemberEvent event) {
        // this method is mainly for revoking the permissions related to
        // being a member from the account
        String permissionPublicName = "special.perm.membership";
        Account account = accountRepository.findByEmail(event.getEmail()).orElseThrow(() -> new IrregularBehaviourException("Account not found"));
        Permission permission = permissionRepository.findByPublicName(permissionPublicName).orElseThrow(() -> new IrregularBehaviourException("Permission not found"));

        account.getPermissions().remove(permission);
        accountRepository.save(account);
    }

    @EventListener(AccountIdentityVerifiedEvent.class)
    public void onAccountIdentityVerifiedEvent(AccountIdentityVerifiedEvent event) {
        // this method is mainly for granting the permissions related to
        // having a verified identity to the account
        String permissionPublicName = "special.perm.verified_identity";
        Account account = accountRepository.findByEmail(event.getEmail()).orElseThrow(() -> new IrregularBehaviourException("Account not found"));
        Permission permission = permissionRepository.findByPublicName(permissionPublicName).orElseThrow(() -> new IrregularBehaviourException("Permission not found"));

        account.getPermissions().add(permission);
        accountRepository.save(account);
    }

    @EventListener(AccountIdentityUnverifiedEvent.class)
    public void onAccountIdentityUnverifiedEvent(AccountIdentityUnverifiedEvent event) {
        // this method is mainly for revoking the permissions related to
        // having a verified identity from the account
        String permissionPublicName = "special.perm.verified_identity";
        Account account = accountRepository.findByEmail(event.getEmail()).orElseThrow(() -> new IrregularBehaviourException("Account not found"));
        Permission permission = permissionRepository.findByPublicName(permissionPublicName).orElseThrow(() -> new IrregularBehaviourException("Permission not found"));

        account.getPermissions().remove(permission);
        accountRepository.save(account);
    }


}
