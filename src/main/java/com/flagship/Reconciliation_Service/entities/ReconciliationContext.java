package com.flagship.Reconciliation_Service.entities;

import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Getter
@Builder
public class ReconciliationContext {

    private LedgerEntry ledger;
//    private WalletSnapshot wallet;
//    private NostroSnapshot nostro;
//    private SwiftConfirmation swift;

//    public boolean amountsMatch() {
//        return ledger.getAmount().compareTo(wallet.getAmount()) == 0
//                && ledger.getAmount().compareTo(nostro.getAmount()) == 0;
//    }
//
//    public boolean currencyMatches() {
//        return ledger.getCurrency().equals(wallet.getCurrency())
//                && ledger.getCurrency().equals(nostro.getCurrency());
//    }
//
//    public boolean isSingleSidedFailure() {
//        return wallet.isInconsistent() ^ nostro.isInconsistent();
//    }
//
    public boolean isSettlementEntry() {
        return ledger.getEntryType() == LedgerEntryType.SETTLEMENT;
    }
//
//    public boolean isOutsideSafetyWindow() {
//        return Duration.between(
//                ledger.getPostedAt(),
//                Instant.now()
//        ).toMinutes() >= 5;
//    }
//
//    public boolean balanceSafe() {
//        return wallet.canApplyRepair(ledger.getAmount());
//    }
}
