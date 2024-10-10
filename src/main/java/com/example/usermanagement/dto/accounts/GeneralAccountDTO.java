package com.example.usermanagement.dto.accounts;

import com.example.usermanagement.entities.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class GeneralAccountDTO {
    private UUID id;
    private String email;
    private String photoUrl;

    public GeneralAccountDTO(Account userAccount) {
        this.id = userAccount.getId();
        this.email = userAccount.getEmail();
        this.photoUrl = userAccount.getPhotoUrl();
    }

    public static List<GeneralAccountDTO> fromAccounts(Set<Account> accounts) {
        return accounts.stream().map(GeneralAccountDTO::new).toList();
    }
}
