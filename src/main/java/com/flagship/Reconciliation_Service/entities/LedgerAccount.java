package com.flagship.Reconciliation_Service.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ledger_account")
@Getter
@NoArgsConstructor
public class LedgerAccount {

    @Id
    @Column(name = "ledger_account_id")
    private UUID ledgerAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerAccountType accountType;

    @Column(nullable = false, length = 3)
    private String currency;
}

