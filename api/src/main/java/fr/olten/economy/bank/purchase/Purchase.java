package fr.olten.economy.bank.purchase;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a purchase from a sender to a receiver.<br>
 * A third parameter is required, which represents the bank, and
 * is used for taxes.
 * @param <S> sender's type (e.g. BankAccount)
 * @param <R> receiver's type (e.g. BankAccount)
 * @param <B> bank's type
 */
public interface Purchase<S, R, B> {

    PurchaseResult purchase(@NotNull S sender,
                            @NotNull R receiver, @NotNull B bank, double amount, double taxesInPercentage);
}
