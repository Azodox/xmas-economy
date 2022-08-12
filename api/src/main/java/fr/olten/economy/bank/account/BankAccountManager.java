package fr.olten.economy.bank.account;

import fr.olten.economy.bank.Bank;
import fr.olten.economy.bank.purchase.Purchase;
import fr.olten.economy.bank.transaction.Transaction;

public interface BankAccountManager {

    Transaction<BankAccount, BankAccount> transaction();

    Purchase<BankAccount, BankAccount, Bank> purchase();

}
