package com.flagship.Reconciliation_Service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class NostroAccount {

    @Id
    private UUID id;

    private String currency;

    private BigDecimal availableBalance;


}
