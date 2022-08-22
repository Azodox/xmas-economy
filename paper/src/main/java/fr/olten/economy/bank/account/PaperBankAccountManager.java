package fr.olten.economy.bank.account;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.olten.economy.bank.BalanceType;
import fr.olten.economy.bank.BalanceUpdateResult;
import fr.olten.economy.bank.Bank;
import fr.olten.economy.bank.conversion.Conversion;
import fr.olten.economy.bank.conversion.RsToFlcConversion;
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

    @Override
    public Conversion conversion() {
        return new RsToFlcConversion();
    }

    public boolean increaseBalanceWouldFail(BalanceType type, double amount) {
        return this.account.getBalance(type) + amount > this.account.getMaxBalance(type);
    }

    public boolean decreaseBalanceWouldFail(BalanceType type,  double amount) {
        return this.account.getBalance(type) - amount < this.account.getMinBalance(type);
    }

    @Override
    public BalanceUpdateResult increaseBalance(BalanceType type,  double amount) {
        if (this.account.getBalance(type) + amount > this.account.getMaxBalance(type)) {
            return new BalanceUpdateResult(false, BalanceUpdateResult.BalanceUpdateFailedReason.OVER_MAX_BALANCE);
        }

        this.datastore.find(BankAccount.class).filter(Filters.eq("_id", this.account.get_id())).
                update(UpdateOperators.inc("balance", amount)).execute();
        return new BalanceUpdateResult(true, null);
    }

    public BalanceUpdateResult decreaseBalance(BalanceType type, double amount) {
        if (this.account.getBalance(type) - amount < this.account.getMinBalance(type)) {
            return new BalanceUpdateResult(false, BalanceUpdateResult.BalanceUpdateFailedReason.UNDER_MIN_BALANCE);
        }

        this.datastore.find(BankAccount.class).filter(Filters.eq("_id", this.account.get_id())).
                update(UpdateOperators.dec("balance", amount)).execute();
        return new BalanceUpdateResult(true, null);
    }
}
