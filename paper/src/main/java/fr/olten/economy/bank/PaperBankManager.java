package fr.olten.economy.bank;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import fr.olten.economy.bank.account.BankAccount;
import fr.olten.economy.bank.account.BankAccountManager;
import fr.olten.economy.bank.account.PaperBankAccountManager;
import org.bson.types.ObjectId;

import java.util.UUID;

public class PaperBankManager implements BankManager {

    private final Datastore datastore;

    public PaperBankManager(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public Bank getBank(String name) {
        return datastore.find(Bank.class).filter(Filters.eq("name", name)).first();
    }

    @Override
    public Bank getBank(ObjectId _id) {
        return datastore.find(Bank.class).filter(Filters.eq("_id", _id)).first();
    }

    @Override
    public BankAccount addAccount(Bank bank, BankAccount account) {
        bank.getAccounts().put(account.getOwner(), account.get_id());
        datastore.merge(bank);
        return account;
    }

    @Override
    public BankAccount getAccount(UUID uuid) {
        return datastore.find(BankAccount.class).filter(Filters.eq("owner", uuid)).first();
    }

    @Override
    public BankAccountManager manageAccount(BankAccount account) {
        return new PaperBankAccountManager(datastore, account);
    }
}
