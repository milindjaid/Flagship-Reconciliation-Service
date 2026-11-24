package com.flagship.Reconciliation_Service.repos;

import com.flagship.Reconciliation_Service.entities.WalletTransaction;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletTransactionRepository
        extends JpaRepository<@NonNull WalletTransaction, @NonNull UUID> {

    @Query("""
        SELECT COUNT(w) > 0 FROM WalletTransaction w
        WHERE w.walletId = :walletId
          AND w.paymentId = :paymentId
          AND w.direction = 'DEBIT'
          AND w.amount = :amount
    """)
    boolean existsDebit(
            UUID walletId,
            UUID paymentId,
            BigDecimal amount
    );
}
