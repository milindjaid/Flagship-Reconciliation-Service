package com.flagship.Reconciliation_Service.services;

import com.flagship.Reconciliation_Service.entities.Wallet;
import com.flagship.Reconciliation_Service.entities.WalletTransaction;
import com.flagship.Reconciliation_Service.repos.WalletRepository;
import com.flagship.Reconciliation_Service.repos.WalletTransactionRepository;
import com.flagship.common_lib.dto.PaymentDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletRepairService {

    private final PaymentClient paymentClient;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository txRepository;

    @Transactional
    public void debitWallet(UUID paymentId) {

        // 1️⃣ Fetch authoritative payment data
        PaymentDetails payment = paymentClient.getPayment(paymentId);

        // 2️⃣ Load wallet deterministically
        Wallet wallet =
                walletRepository.findByCustomerIdAndCurrency(
                        payment.customerId(),
                        payment.currency()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Wallet not found for reconciliation"
                        )
                );

        // 3️⃣ Idempotency check (CRITICAL)
        boolean alreadyDebited =
                txRepository.existsDebit(
                        wallet.getWalletId(),
                        paymentId,
                        payment.amount()
                );

        if (alreadyDebited) {
            return; // safe retry
        }

        // 4️⃣ Safety check — do NOT over-debit
        if (!wallet.canDebit(payment.amount())) {
            throw new IllegalStateException(
                    "Insufficient wallet balance during repair"
            );
        }

        // 5️⃣ Apply debit
        wallet.debit(payment.amount());
        walletRepository.save(wallet);

        // 6️⃣ Persist audit transaction
        txRepository.save(
                WalletTransaction.debit(
                        wallet.getWalletId(),
                        paymentId,
                        payment.amount(),
                        payment.currency()
                )
        );
    }

}

