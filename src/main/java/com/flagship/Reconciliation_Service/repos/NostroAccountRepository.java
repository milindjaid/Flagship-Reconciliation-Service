package com.flagship.Reconciliation_Service.repos;

import com.flagship.Reconciliation_Service.entities.NostroAccount;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NostroAccountRepository extends JpaRepository<@NonNull NostroAccount,@NonNull UUID> {
    Optional<NostroAccount> findByCurrency(String currency);
}
