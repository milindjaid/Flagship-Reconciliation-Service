package com.flagship.Reconciliation_Service.entities;

import com.flagship.Reconciliation_Service.constants.Direction;
import com.flagship.Reconciliation_Service.constants.TxStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wallet_transaction",
        indexes = @Index(name = "idx_wallet_tx_payment", columnList = "paymentId"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletTransaction {

    @Id
    private UUID transactionId;

    private UUID walletId;
    private UUID paymentId;

    private BigDecimal amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private Direction direction; // DEBIT / CREDIT

    @Enumerated(EnumType.STRING)
    private TxStatus status; // POSTED / REVERSED

    private Instant createdAt;

    public static WalletTransaction debit(UUID walletId,
                                          UUID paymentId,
                                          BigDecimal amount,
                                          String currency) {

        WalletTransaction tx = new WalletTransaction();
        tx.transactionId = UUID.randomUUID();
        tx.walletId = walletId;
        tx.paymentId = paymentId;
        tx.amount = amount;
        tx.currency = currency;
        tx.direction = Direction.DEBIT;
        tx.status = TxStatus.POSTED;
        tx.createdAt = Instant.now();
        return tx;
    }
}
