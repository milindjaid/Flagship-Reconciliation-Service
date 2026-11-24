package com.flagship.Reconciliation_Service.services;

import com.flagship.Reconciliation_Service.proto.PaymentQueryServiceGrpc;
import com.flagship.common_lib.dto.PaymentDetails;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentClient {

    private final com.flagship.Reconciliation_Service.proto.PaymentQueryServiceGrpc.PaymentQueryServiceBlockingStub stub;

    // forAddress("payment-service", 9090)
//(or payment-service.namespace.svc.cluster.local if you’re being explicit) for k8s
    public PaymentClient() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090) // if šervice is on same machine
                .usePlaintext() // ⚠️ remove in prod (use TLS)
                .build();

        this.stub = PaymentQueryServiceGrpc.newBlockingStub(channel);
    }

    public PaymentDetails getPayment(UUID paymentId) {

        com.flagship.Reconciliation_Service.proto.PaymentRequest request = com.flagship.Reconciliation_Service.proto.PaymentRequest.newBuilder()
                .setPaymentId(paymentId.toString())
                .build();

        com.flagship.Reconciliation_Service.proto.PaymentResponse response = stub.getPayment(request);

        return new PaymentDetails(
                UUID.fromString(response.getPaymentId()),
                response.getBeneficiaryBic(),
                response.getBeneficiaryAccount(),
                response.getCurrency(),
                new BigDecimal(response.getAmount()),
                response.getCustomerId()
        );
    }
}

