package com.example.usermanagement.interfaces.services;

import com.example.usermanagement.entities.Account;

import java.util.List;
import java.util.UUID;

/*
    Notes:
     - DTO classes should not be returned from service methods.
*/
public interface IAccountService {
    void encodeAndSaveAccount(Account userAccount);
    List<Account> getAllUsers();
    Account getMyAccount();
    void grantRoleToAccount(UUID userId, UUID roleId);
}

