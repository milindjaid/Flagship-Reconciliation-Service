package com.flagship.Reconciliation_Service.repos;

import com.flagship.Reconciliation_Service.entities.ReconciliationCase;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReconciliationCaseRepository extends JpaRepository<@NonNull  ReconciliationCase, @NonNull UUID> {
}
