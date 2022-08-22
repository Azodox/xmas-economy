package fr.olten.economy.bank;

import org.jetbrains.annotations.Nullable;

public record BalanceUpdateResult(boolean success,
                                  @Nullable BalanceUpdateResult.BalanceUpdateFailedReason reason) {

    public static enum BalanceUpdateFailedReason {

        UNDER_MIN_BALANCE,
        OVER_MAX_BALANCE,
        UNKNOWN
    }
}
