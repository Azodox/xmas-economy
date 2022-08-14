package fr.olten.economy.bank.transaction;

import dev.morphia.Datastore;
import fr.olten.economy.bank.account.BankAccount;
import fr.olten.economy.bank.account.PaperBankAccountManager;
import org.jetbrains.annotations.NotNull;

public class PaperTransaction<S extends BankAccount, R extends BankAccount> implements Transaction<S, R> {

    private final Datastore datastore;

    public PaperTransaction(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public TransactionResult execute(@NotNull S sender, @NotNull R receiver, double amount) {
        var senderManager = new PaperBankAccountManager(datastore, sender);
        if(!senderManager.decreaseBalanceWouldFail(amount) && senderManager.decreaseBalance(amount).success()){
            var receiverManager = new PaperBankAccountManager(datastore, receiver);
            if(!receiverManager.increaseBalanceWouldFail(amount)){
                receiverManager.increaseBalance(amount);
                return TransactionResult.SUCCESS;
            }
        }
        return TransactionResult.FAILURE;
    }
}
