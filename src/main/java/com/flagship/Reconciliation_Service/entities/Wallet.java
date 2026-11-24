package com.flagship.Reconciliation_Service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "wallet_account",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "wallet_account_customer_id_currency_key",
                        columnNames = {"customer_id", "currency"}
                )
        },
        indexes = {
                @Index(name = "idx_wallet_customer", columnList = "customer_id")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @Column(name = "wallet_id", nullable = false)
    private UUID walletId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "available_balance", nullable = false, precision = 18, scale = 2)
    private BigDecimal availableBalance;

    @Column(name = "blocked_balance", nullable = false, precision = 18, scale = 2)
    private BigDecimal blockedBalance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WalletStatus status;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // ---------- Lifecycle Hooks ----------

    @PrePersist
    public void onCreate() {
        if (walletId == null) {
            walletId = UUID.randomUUID();
        }
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }

    // ---------- Domain Logic (Money Safety) ----------

    public boolean canDebit(BigDecimal amount) {
        return availableBalance.compareTo(amount) >= 0;
    }

    public void debit(BigDecimal amount) {
        this.availableBalance =
                this.availableBalance.subtract(amount);
    }


}

