package com.flagship.Reconciliation_Service.services;

import com.flagship.common_lib.dto.PaymentDetails;
import com.flagship.common_lib.dto.ReconcileRepairEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NostroRepairService {

//    private final PaymentClient paymentClient;
     private final KafkaTemplate<@NonNull String, @NonNull Object> kafkaTemplate;

    @Transactional
    public void debitNostro(UUID paymentId) {

        PaymentDetails payment = null;
//                paymentClient.getPayment(paymentId);

        kafkaTemplate.send("reconcile.nostro.repair",  new ReconcileRepairEvent( paymentId,
                payment.amount(),
                payment.currency())
        );
    }
}

