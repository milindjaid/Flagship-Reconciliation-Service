package com.flagship.Reconciliation_Service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "repair_attempt")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepairAttempt {

    @Id
    @Column(name = "ledger_entry_id", nullable = false)
    private UUID ledgerEntryId;

    @Column(name = "attempted_at", nullable = false)
    private Instant attemptedAt;

    @Column(name = "decision", nullable = false)
    private String decision; // AUTO_REPAIRED / ESCALATED

    @Column(name = "reason")
    private String reason;
}


