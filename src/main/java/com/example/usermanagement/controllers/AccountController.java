package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.accounts.*;
import com.example.usermanagement.entities.Account;
import com.example.usermanagement.interfaces.services.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<GeneralAccountDTO>> getAccounts(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<Account> accounts = accountService.searchAndSortAccounts(email, sort, page, size, direction);
        return new ResponseEntity<>(accounts.map(GeneralAccountDTO::new), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UUID> createAccount(@RequestBody CreateAccountDTO requestBody) {
        Account userAccount = requestBody.toEntity(null);
        accountService.encodeAndSaveAccount(userAccount);
        return new ResponseEntity<>(userAccount.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<GeneralAccountDTO> getMe() {
        var account = new GeneralAccountDTO(accountService.getMyAccount());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<DetailedAccountDTO> getAccount(@PathVariable UUID accountId) {
        var account = new DetailedAccountDTO(accountService.getAccountById(accountId));
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/authorities")
    public ResponseEntity<List<AccountAuthoritiesEditResponse>> editAuthorities(@RequestBody List<EditAuthoritiesRequest> requestBody) {
        List<AccountAuthoritiesEditResponse> res = accountService.editAuthorities(requestBody);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
