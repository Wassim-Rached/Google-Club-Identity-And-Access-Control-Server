package com.example.usermanagement.dto.accounts;

import com.example.usermanagement.entities.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SafeAccountInfo {
    private String email;

    public SafeAccountInfo(Account userAccount) {
        this.email = userAccount.getEmail();
    }

}
