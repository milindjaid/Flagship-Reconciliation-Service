package com.flagship.Reconciliation_Service.repos;

import com.flagship.Reconciliation_Service.entities.LedgerEntry;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public interface LedgerEntryRepository
        extends JpaRepository<@NonNull LedgerEntry, @NonNull UUID> {

    @Query("""
        SELECT l FROM LedgerEntry l
        WHERE l.status = 'POSTED'
          AND l.createdAt >= :from
    """)
    List<LedgerEntry> findPostedSince(
            @Param("from") Instant from
    );

    default List<LedgerEntry> findPostedLast24Hours() {
        return findPostedSince(Instant.now().minus(24, ChronoUnit.HOURS));
    }
}
