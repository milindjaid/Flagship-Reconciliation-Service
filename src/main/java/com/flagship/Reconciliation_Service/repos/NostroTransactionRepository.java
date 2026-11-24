package com.flagship.Reconciliation_Service.repos;

import com.flagship.Reconciliation_Service.entities.NostroTransaction;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface NostroTransactionRepository
        extends JpaRepository<@NonNull NostroTransaction, @NonNull UUID> {

    @Query("""
        SELECT COUNT(n) > 0 FROM NostroTransaction n
        WHERE n.paymentId = :paymentId
          AND n.direction = 'DEBIT'
          AND n.amount = :amount
          AND n.currency = :currency
    """)
    boolean existsDebit(
            UUID paymentId,
            BigDecimal amount,
            String currency
    );
}

