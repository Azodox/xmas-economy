package fr.olten.economy.bank;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;

public class BankBalanceManager {

    private final Datastore datastore;
    private final Bank bank;

    public BankBalanceManager(Datastore datastore, Bank bank) {
        this.datastore = datastore;
        this.bank = bank;
    }

    public BalanceUpdateResult increaseBalance(double amount){
        var query = datastore.find(Bank.class).filter(Filters.eq("_id", this.bank.get_id()));
        if (query.update(UpdateOperators.inc("balance", amount)).execute().getModifiedCount() > 0) {
            return new BalanceUpdateResult(true, null);
        }
        return new BalanceUpdateResult(false, BalanceUpdateResult.BalanceUpdateFailedReason.UNKNOWN);
    }
}
