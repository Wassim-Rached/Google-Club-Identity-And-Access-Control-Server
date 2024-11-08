package com.ics.interfaces.services;

import com.ics.dto.accounts.AccountAuthoritiesEditResponse;
import com.ics.dto.accounts.EditAuthoritiesRequest;
import com.ics.dto.accounts.UpdateAccountDTO;
import com.ics.entities.Account;
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
    Account getAccountById(UUID accountId);
    Account getAccountByEmail(String email);

    List<AccountAuthoritiesEditResponse> editAuthorities(List<EditAuthoritiesRequest> requestBody);
    void verifyIdentity(boolean isVerified, Account account);

    Account getMyAccount();
    void deleteMyAccount(String password);
    Account updateMyAccount(UpdateAccountDTO requestBody);

    void resetPassword(Account account,String newPassword);
    void changeMyPassword(String oldPassword, String newPassword);
    void verifyAccountEmail(String accountEmail);

    void lockAccount(boolean lock, Account account);

    void changeMembership(boolean member, Account account);
}

