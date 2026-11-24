package com.flagship.Reconciliation_Service.publisher;

import com.flagship.Reconciliation_Service.constants.ReconMismatchType;
import com.flagship.Reconciliation_Service.entities.LedgerEntry;
import com.flagship.common_lib.dto.ReconciliationMismatchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ReconAlertPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishMismatch(ReconMismatchType type,
                                LedgerEntry entry,
                                String reason) {

        kafkaTemplate.send(
                "reconciliation.mismatch",
                new ReconciliationMismatchEvent(
                        entry.getPaymentId(),
                        entry.getLedgerEntryId(),
                        type.name(),
                        reason,
                        Instant.now()
                )
        );
    }

    public void publishSystemError(LedgerEntry entry, Exception ex) {

        kafkaTemplate.send(
                "reconciliation.system.error",
                new ReconciliationMismatchEvent(
                        entry.getPaymentId(),
                        entry.getLedgerEntryId(),
                        ReconMismatchType.SYSTEM_ERROR.name(),
                        ex.getMessage(),
                        Instant.now()
                )
        );
    }
}

