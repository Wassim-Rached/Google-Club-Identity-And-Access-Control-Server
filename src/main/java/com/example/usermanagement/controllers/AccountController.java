package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.accounts.CreateAccountDTO;
import com.example.usermanagement.dto.accounts.EditAuthoritiesRequest;
import com.example.usermanagement.dto.accounts.SafeAccountInfo;
import com.example.usermanagement.entities.Account;
import com.example.usermanagement.interfaces.services.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final IAccountService accountService;

    @GetMapping
    public ResponseEntity<List<SafeAccountInfo>> getUsers() {
        var accounts = accountService.getAllAccounts().stream()
                .map(SafeAccountInfo::new)
                .toList();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UUID> createUser(@RequestBody CreateAccountDTO requestBody) {
        Account userAccount = requestBody.toEntity(null);
        accountService.encodeAndSaveAccount(userAccount);
        return new ResponseEntity<>(userAccount.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<SafeAccountInfo> getMe() {
        var account = new SafeAccountInfo(accountService.getMyAccount());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/{accountId}/authorities")
    public ResponseEntity<Void> editAuthorities(@PathVariable UUID accountId, @RequestBody EditAuthoritiesRequest requestBody) {
        accountService.editAuthorities(accountId, requestBody);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
