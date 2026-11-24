package com.flagship.Reconciliation_Service.services;

import com.flagship.Reconciliation_Service.constants.Direction;
import com.flagship.Reconciliation_Service.constants.ReconMismatchType;
import com.flagship.Reconciliation_Service.entities.*;
import com.flagship.Reconciliation_Service.publisher.ReconAlertPublisher;
import com.flagship.Reconciliation_Service.repos.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Settlement completes processing; reconciliation validates financial truth.
//Reconciliation builds a decision context using read-only snapshots from Ledger, Wallet, Nostro, and SWIFT, then applies deterministic repair rules.

@Service
@RequiredArgsConstructor
public class ReconciliationService {

    private final LedgerEntryRepository ledgerRepository;
    private final WalletTransactionRepository walletTxRepository;
    private final NostroTransactionRepository nostroTxRepository;
    private final ReconciliationCaseRepository caseRepository;
    private final AutoRepairDecisionService decisionService;
    private final RepairExecutor repairExecutor;
    private final WalletRepository walletRepository;
    private final NostroAccountRepository nostroAccountRepository;
    private final ReconAlertPublisher reconAlertPublisher;

    @Scheduled(cron = "0 0 * * * ?") // hourly
    public void reconcile() {

        // 1️⃣ Fetch authoritative accounting truth
        List<LedgerEntry> ledgerEntries =
                ledgerRepository.findPostedLast24Hours();

        for (LedgerEntry ledger : ledgerEntries) {

            UUID paymentId = ledger.getPaymentId();

            // 2️⃣ Wallet check
            boolean walletDebited =
                    walletTxRepository.existsDebit(
                            ledger.getDebitAccountId(),
                            paymentId,
                            ledger.getAmount()
                    );

            // 3️⃣ Nostro check
            boolean nostroDebited =
                    nostroTxRepository.existsDebit(
                            paymentId,
                            ledger.getAmount(),
                            ledger.getCurrency()
                    );

         //  Optional<@NonNull Wallet> wallet  = walletRepository.findById(ledger.getDebitAccountId());

            ReconciliationContext ctx =
                    ReconciliationContext.builder()
                            .ledger(ledger)
//                            .wallet(wallet.get())
//                            .nostro(nostro)
//                            .swift(swift)
                            .build();


            if (!decisionService.canAutoRepair(ctx)) {
                reconAlertPublisher.publishMismatch(ReconMismatchType.LEDGER_MISSING,ledger,"Ledger is posted.");
                return; // escalate to ops elsewhere
            }


            // 4️⃣ Detect mismatches
            if (!walletDebited) {
                createAndRepair(
                        paymentId,
                        ReconMismatchType.WALLET_NOT_DEBITED,
                        ledger
                );
            }

            if (!nostroDebited) {
                createAndRepair(
                        paymentId,
                        ReconMismatchType.NOSTRO_NOT_DEBITED,
                        ledger
                );
            }
        }
    }

    private void createAndRepair(
            UUID paymentId,
            ReconMismatchType type,
            LedgerEntry ledger
    ) {
        ReconciliationCase rc =
                caseRepository.save(
                        ReconciliationCase.open(
                                paymentId,
                                type,
                                ledger
                        )
                );

        repairExecutor.execute(rc);
    }
}







