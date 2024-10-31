package com.ics.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "password_reset_tokens")
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false, name = "expiry_date")
    private Instant expiryDate;

    @Column(nullable = false, name = "created_date")
    private Instant createdDate;

    @Column(nullable = false, name = "is_used")
    private Boolean isUsed;

    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

    public PasswordResetToken(Account account, int expiryTimeInSec) {
        this.token = UUID.randomUUID().toString();
        this.createdDate = Instant.now();
        this.expiryDate = this.createdDate.plusSeconds(expiryTimeInSec);
        this.isUsed = false;
        this.account = account;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }

    public boolean haveBeenCreatedLately(int seconds) {
        return Instant.now().isBefore(this.createdDate.plusSeconds(seconds));
    }

}