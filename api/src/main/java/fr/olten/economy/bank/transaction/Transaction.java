package fr.olten.economy.bank.transaction;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a transaction from a sender to a receiver.<br>
 * Usually, a transaction is made between two banks accounts.
 * @param <S> sender's type (e.g. BankAccount)
 * @param <R> receiver's type (e.g. BankAccount)
 */
public interface Transaction<S, R> {

    TransactionResult execute(@NotNull S sender, @NotNull R receiver, double amount);
}
