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
@Table(name = "nostro_transaction",
        indexes = @Index(name = "idx_nostro_tx_payment", columnList = "paymentId"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NostroTransaction {

    @Id
    private UUID transactionId;

    private UUID nostroAccountId;
    private UUID paymentId;

    private BigDecimal amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    private TxStatus status;

    private Instant createdAt;

    public static NostroTransaction debit(UUID nostroId,
                                          UUID paymentId,
                                          BigDecimal amount,
                                          String currency) {

        NostroTransaction tx = new NostroTransaction();
        tx.transactionId = UUID.randomUUID();
        tx.nostroAccountId = nostroId;
        tx.paymentId = paymentId;
        tx.amount = amount;
        tx.currency = currency;
        tx.direction = Direction.DEBIT;
        tx.status = TxStatus.POSTED;
        tx.createdAt = Instant.now();
        return tx;
    }
}
