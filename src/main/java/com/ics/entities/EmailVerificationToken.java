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
@Table(name = "email_verification_tokens")
@NoArgsConstructor
public class EmailVerificationToken {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false,name = "expiry_date")
    private Instant expiryDate;

    @Column(nullable = false,name = "created_date")
    private Instant createdDate;

    @Column(nullable = false,name = "is_used")
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

    @Override
    public String toString() {
        return "EmailVerificationToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                ", createdDate=" + createdDate +
                ", isUsed=" + isUsed +
                ", account=" + account +
                '}';
    }
}
