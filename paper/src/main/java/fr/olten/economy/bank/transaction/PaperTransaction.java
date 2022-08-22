package fr.olten.economy.bank.transaction;

import dev.morphia.Datastore;
import fr.olten.economy.bank.BalanceType;
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
        if(!senderManager.decreaseBalanceWouldFail(BalanceType.FLC, amount) &&
                senderManager.decreaseBalance(BalanceType.FLC, amount).success()){
            var receiverManager = new PaperBankAccountManager(datastore, receiver);
            if(!receiverManager.increaseBalanceWouldFail(BalanceType.FLC, amount)){
                receiverManager.increaseBalance(BalanceType.FLC, amount);
                return TransactionResult.SUCCESS;
            }
        }
        return TransactionResult.FAILURE;
    }
}
