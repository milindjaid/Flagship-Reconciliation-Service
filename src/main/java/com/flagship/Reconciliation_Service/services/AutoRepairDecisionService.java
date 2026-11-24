package com.flagship.Reconciliation_Service.services;

import com.flagship.Reconciliation_Service.entities.ReconciliationContext;
import com.flagship.Reconciliation_Service.entities.RepairAttempt;
import com.flagship.Reconciliation_Service.repos.RepairAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutoRepairDecisionService {

    private final RepairAttemptRepository repairAttemptRepository;

    public boolean canAutoRepair(ReconciliationContext ctx) {

        if (!ctx.getLedger().isPosted()) return false;
//        if (!ctx.getSwift().isSettled()) return false;
//        if (!ctx.amountsMatch()) return false;
//        if (!ctx.currencyMatches()) return false;
//        if (!ctx.isSingleSidedFailure()) return false;
//        if (!ctx.isOutsideSafetyWindow()) return false;
//        if (!ctx.isSettlementEntry()) return false;
//        if (!ctx.balanceSafe()) return false;

        return repairAttemptRepository
                .findById(ctx.getLedger().getLedgerEntryId())
                .isEmpty();
    }

    public void markAttempted(UUID ledgerEntryId) {
        repairAttemptRepository.save(
                new RepairAttempt(
                        ledgerEntryId,
                        Instant.now(),
                        "ESCALATED",
                        "Test"
                )
        );
    }
}

