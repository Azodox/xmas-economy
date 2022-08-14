package fr.olten.economy.bank;

import fr.olten.economy.bank.account.BankAccount;
import fr.olten.economy.bank.account.BankAccountManager;
import org.bson.types.ObjectId;

import java.util.UUID;

public interface BankManager {

    Bank getBank(String name);
    Bank getBank(ObjectId _id);
    Bank addBank(Bank bank);

    BankAccount addAccount(Bank bank, BankAccount account);
    BankAccount getAccount(UUID uuid);
    BankAccount getAccount(ObjectId _id);
    BankAccountManager manageAccount(BankAccount account);
}
