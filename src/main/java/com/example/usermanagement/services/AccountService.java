package com.example.usermanagement.services;

import com.example.usermanagement.entities.Role;
import com.example.usermanagement.entities.Account;
import com.example.usermanagement.interfaces.services.IAccountService;
import com.example.usermanagement.repositories.RoleRepository;
import com.example.usermanagement.repositories.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void encodeAndSaveAccount(Account userAccount) {
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        accountRepository.save(userAccount);
    }

    @Override
    public List<Account> getAllUsers() {
        return accountRepository.findAll();
    }

    @Override
    public Account getMyAccount() {
        var userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("userId = " + userId);
        return accountRepository.findById((UUID) userId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void grantRoleToAccount(UUID userId, UUID roleId) {
        Account userAccount = accountRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Role role = roleRepository.findById(roleId).orElseThrow(EntityNotFoundException::new);
        userAccount.getRoles().add(role);

        accountRepository.save(userAccount);
    }
}
