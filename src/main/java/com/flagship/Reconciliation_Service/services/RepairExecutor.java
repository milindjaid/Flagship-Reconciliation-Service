package com.flagship.Reconciliation_Service.services;

import com.flagship.Reconciliation_Service.entities.ReconciliationCase;
import com.flagship.Reconciliation_Service.repos.ReconciliationCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepairExecutor {

    private final WalletRepairService walletRepairService;
    private final NostroRepairService nostroRepairService;
    private final ReconciliationCaseRepository caseRepository;

    @Transactional
    public void execute(ReconciliationCase rc) {

        switch (rc.getMismatchType()) {

            case WALLET_NOT_DEBITED -> {
                walletRepairService.debitWallet(rc.getPaymentId());
            }

            case NOSTRO_NOT_DEBITED -> {
                nostroRepairService.debitNostro(rc.getPaymentId());
            }

            default -> {
                rc.escalate();
                caseRepository.save(rc);
                return;
            }
        }

        rc.markRepaired();
        caseRepository.save(rc);
    }
}

