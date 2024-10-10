package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.dto.accounts.AccountAuthoritiesEditResponse;
import com.example.usermanagement.dto.accounts.EditAuthoritiesRequest;
import com.example.usermanagement.entities.Account;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/*
    Notes:
     - DTO classes should not be returned from service methods.
*/
public interface IAccountService {
    void encodeAndSaveAccount(Account userAccount);
    Page<Account> searchAndSortAccounts(String email, String sort, int page, int size, String direction);
    Account getMyAccount();
    List<AccountAuthoritiesEditResponse> editAuthorities(List<EditAuthoritiesRequest> requestBody);
    Account getAccountById(UUID accountId);
}

