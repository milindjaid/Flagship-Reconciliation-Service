package com.flagship.Reconciliation_Service.repos;

import com.flagship.Reconciliation_Service.entities.RepairAttempt;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepairAttemptRepository
        extends JpaRepository<@NonNull RepairAttempt, @NonNull UUID> {
}

