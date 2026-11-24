package com.flagship.Reconciliation_Service.consumer;

import com.flagship.common_lib.dto.SwiftRejectedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReconcileConsumer {


    @KafkaListener(topics = "swift.reconcile")
    public void onSwiftReconcile(SwiftRejectedEvent event) {

    }
}
