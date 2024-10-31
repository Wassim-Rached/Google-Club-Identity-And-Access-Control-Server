package com.ics.dto.accounts;

import com.ics.entities.Account;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class GeneralAccountDTO {
    private UUID id;
    private String email;
    private String photoUrl;
    private Boolean isEmailVerified;
    private Boolean isLocked;
    private Boolean isIdentityVerified;
    private Boolean isMember;
    private Instant createdAt;

    public GeneralAccountDTO(Account userAccount) {
        this.id = userAccount.getId();
        this.email = userAccount.getEmail();
        this.photoUrl = userAccount.getPhotoUrl();
        this.isEmailVerified = userAccount.getIsEmailVerified();
        this.isLocked = userAccount.getIsLocked();
        this.isIdentityVerified = userAccount.getIsIdentityVerified();
        this.isMember = userAccount.getIsMember();
        this.createdAt = userAccount.getCreatedAt();
    }

    public static List<GeneralAccountDTO> fromAccounts(Set<Account> accounts) {
        return accounts.stream().map(GeneralAccountDTO::new).toList();
    }
}
