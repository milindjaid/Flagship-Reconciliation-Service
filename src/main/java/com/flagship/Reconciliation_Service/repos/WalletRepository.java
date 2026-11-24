package com.flagship.Reconciliation_Service.repos;

import com.flagship.Reconciliation_Service.entities.Wallet;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<@NonNull Wallet,@NonNull UUID> {

    Optional<@NonNull Wallet> findByCustomerIdAndCurrency(String customerId,String currency);
}
