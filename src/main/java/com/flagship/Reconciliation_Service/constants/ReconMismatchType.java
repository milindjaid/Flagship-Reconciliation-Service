package com.flagship.Reconciliation_Service.constants;

public enum ReconMismatchType {
    WALLET_MISMATCH,
    NOSTRO_MISMATCH,
    SYSTEM_ERROR,
    WALLET_NOT_DEBITED,
    WALLET_OVER_DEBITED,
    NOSTRO_NOT_DEBITED,
    NOSTRO_OVER_DEBITED,
    LEDGER_MISSING,
    DUPLICATE_POSTING
}
