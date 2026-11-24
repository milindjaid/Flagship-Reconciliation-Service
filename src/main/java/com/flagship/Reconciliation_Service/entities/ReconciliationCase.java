package com.flagship.Reconciliation_Service.entities;

import com.flagship.Reconciliation_Service.constants.ReconMismatchType;
import com.flagship.Reconciliation_Service.constants.ReconciliationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reconciliation_case")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReconciliationCase {

    @Id
    private UUID caseId;

    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    private ReconMismatchType mismatchType;

    @Enumerated(EnumType.STRING)
    private ReconciliationStatus status;

    private Instant detectedAt;
    private Instant repairedAt;

    @Column(length = 2000)
    private String details;

    /* ---------------- DOMAIN METHODS ---------------- */

    public static ReconciliationCase open(
            UUID paymentId,
            ReconMismatchType type,
            LedgerEntry ledger
    ) {
        ReconciliationCase rc = new ReconciliationCase();
        rc.caseId = UUID.randomUUID();
        rc.paymentId = paymentId;
        rc.mismatchType = type;
        rc.status = ReconciliationStatus.OPEN;
        rc.detectedAt = Instant.now();
        rc.details = buildDetails(type, ledger);
        return rc;
    }

    public void markRepaired() {
        this.status = ReconciliationStatus.REPAIRED;
        this.repairedAt = Instant.now();
    }

    public void escalate() {
        this.status = ReconciliationStatus.ESCALATED;
    }

    private static String buildDetails(
            ReconMismatchType type,
            LedgerEntry ledger
    ) {
        return """
                Mismatch: %s
                LedgerEntryId: %s
                Amount: %s %s
                """.formatted(
                type,
                ledger.getLedgerEntryId(),
                ledger.getAmount(),
                ledger.getCurrency()
        );
    }
}

