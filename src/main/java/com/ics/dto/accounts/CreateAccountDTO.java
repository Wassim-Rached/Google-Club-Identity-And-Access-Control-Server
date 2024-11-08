package com.ics.dto.accounts;

import com.ics.entities.Account;
import com.ics.interfaces.dto.IEntityDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateAccountDTO implements IEntityDTO<Account,Void>{
    private String email;
    private String password;

    @Override
    public Account toEntity(Void additionalData) {
        var account = new Account();
        account.setEmail(email);
        account.setPassword(password);
        return account;
    }
}
