package com.ics.events.listeners;

import com.ics.entities.Account;
import com.ics.entities.Permission;
import com.ics.events.publishers.*;
import com.ics.exceptions.IrregularBehaviourException;
import com.ics.repositories.AccountRepository;
import com.ics.repositories.PermissionRepository;
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
