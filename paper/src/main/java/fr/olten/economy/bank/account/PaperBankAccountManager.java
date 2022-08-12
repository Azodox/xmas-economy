package fr.olten.economy.bank.account;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.olten.economy.bank.BalanceUpdateResult;
import fr.olten.economy.bank.Bank;
import fr.olten.economy.bank.purchase.PaperPurchase;
import fr.olten.economy.bank.purchase.Purchase;
import fr.olten.economy.bank.transaction.PaperTransaction;
import fr.olten.economy.bank.transaction.Transaction;

public class PaperBankAccountManager implements BankAccountManager {

    private final Datastore datastore;
    private final BankAccount account;

    public PaperBankAccountManager(Datastore datastore, BankAccount account) {
        this.datastore = datastore;
        this.account = account;
    }

    @Override
    public Transaction<BankAccount, BankAccount> transaction() {
        return new PaperTransaction<>(this.datastore);
    }

    @Override
    public Purchase<BankAccount, BankAccount, Bank> purchase() {
        return new PaperPurchase<>(this.datastore);
    }

    public boolean increaseBalanceWouldFail(double amount) {
        return this.account.getBalance() + amount > this.account.getMaxBalance();
    }

    public boolean decreaseBalanceWouldFail(double amount) {
        return this.account.getBalance() - amount < this.account.getMinBalance();
    }

    public BalanceUpdateResult increaseBalance(double amount) {
        if (this.account.getBalance() + amount > this.account.getMaxBalance()) {
            return new BalanceUpdateResult(false, BalanceUpdateResult.BalanceUpdateFailedReason.OVER_MAX_BALANCE);
        }

        this.datastore.find(BankAccount.class).filter(Filters.eq("_id", this.account.get_id())).
                update(UpdateOperators.inc("balance", amount)).execute();
        return new BalanceUpdateResult(true, null);
    }

    public BalanceUpdateResult decreaseBalance(double amount) {
        if (this.account.getBalance() - amount < this.account.getMinBalance()) {
            return new BalanceUpdateResult(false, BalanceUpdateResult.BalanceUpdateFailedReason.UNDER_MIN_BALANCE);
        }

        this.datastore.find(BankAccount.class).filter(Filters.eq("_id", this.account.get_id())).
                update(UpdateOperators.dec("balance", amount)).execute();
        return new BalanceUpdateResult(true, null);
    }
}
