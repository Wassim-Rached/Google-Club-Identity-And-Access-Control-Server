package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.StandardApiResponse;
import com.example.usermanagement.dto.accounts.CreateAccountDTO;
import com.example.usermanagement.dto.accounts.SafeAccountInfo;
import com.example.usermanagement.entities.Account;
import com.example.usermanagement.services.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<StandardApiResponse<List<SafeAccountInfo>>> getUsers() {
        var accounts = accountService.getAllUsers().stream()
                .map(SafeAccountInfo::new)
                .toList();
        return new ResponseEntity<>(new StandardApiResponse<>(accounts), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StandardApiResponse<UUID>> createUser(@RequestBody CreateAccountDTO requestBody) {
        Account userAccount = requestBody.toEntity(null);
        accountService.encodeAndSaveAccount(userAccount);
        return new ResponseEntity<>(new StandardApiResponse<>(userAccount.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<StandardApiResponse<SafeAccountInfo>> getMe() {
        var account = new SafeAccountInfo(accountService.getMyAccount());
        return new ResponseEntity<>(new StandardApiResponse<>(account), HttpStatus.OK);
    }

    @PostMapping("/{accountId}/roles")
    public ResponseEntity<StandardApiResponse<Void>> grantRoleToAccount(@PathVariable UUID accountId, @RequestBody UUID roleId) {
        accountService.grantRoleToAccount(accountId, roleId);
        return new ResponseEntity<>(new StandardApiResponse<>(), HttpStatus.OK);
    }
}
