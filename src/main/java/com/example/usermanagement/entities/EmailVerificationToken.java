package com.example.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "email_verification_tokens")
@NoArgsConstructor
public class EmailVerificationToken {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private Instant createdDate;

    @Column(nullable = false)
    private boolean isUsed;

    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

    public EmailVerificationToken(Account account,int expiryTimeInSec) {
        this.token = UUID.randomUUID().toString();
        this.createdDate = Instant.now();
        this.expiryDate = this.createdDate.plusSeconds(expiryTimeInSec);
        this.isUsed = false;
        this.account = account;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }

    public boolean createdInLastSeconds(int seconds) {
        return Instant.now().isBefore(this.createdDate.plusSeconds(seconds));
    }
}
