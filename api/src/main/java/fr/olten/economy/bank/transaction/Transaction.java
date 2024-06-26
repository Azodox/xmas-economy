package fr.olten.economy.bank.transaction;

/**
 * Represents a transaction from a sender to a receiver.<br>
 * Usually, a transaction is made between two banks accounts.
 * @param <S> sender's type (e.g. BankAccount)
 * @param <R> receiver's type (e.g. BankAccount)
 */
public interface Transaction<S, R> {

    void take(S sender, long amount);
    void give(R receiver, long amount);
}
