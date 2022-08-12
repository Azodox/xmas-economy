package fr.olten.economy.bank.purchase;

import fr.olten.economy.bank.transaction.Transaction;

/**
 * Represents a purchase from a sender to a receiver.<br>
 * A third parameter is required, which represents the bank,
 * is used for taxes.
 * @param <S> sender's type (e.g. BankAccount)
 * @param <R> receiver's type (e.g. BankAccount)
 * @param <B> bank's type
 */
public interface Purchase<S, R, B> extends Transaction<S, R> {

    void purchase(S sender, R receiver, B bank, double taxesInPercentage);
}
