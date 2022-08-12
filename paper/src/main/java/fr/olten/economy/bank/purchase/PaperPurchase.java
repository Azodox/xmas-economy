package fr.olten.economy.bank.purchase;

import dev.morphia.Datastore;
import fr.olten.economy.bank.Bank;
import fr.olten.economy.bank.BankBalanceManager;
import fr.olten.economy.bank.account.BankAccount;
import fr.olten.economy.bank.account.PaperBankAccountManager;
import fr.olten.economy.bank.transaction.TransactionResult;

public class PaperPurchase<S extends BankAccount, R extends BankAccount, B extends Bank> implements Purchase<S, R, B> {

    private final Datastore datastore;

    public PaperPurchase(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public PurchaseResult purchase(S sender, R receiver, B bank, double amount, double taxesInPercentage) {
        var senderManager = new PaperBankAccountManager(datastore, sender);
        var amountAfterTaxes = (amount / 100) * taxesInPercentage;
        var amountDelta = amount - amountAfterTaxes;

        if (senderManager.transaction().execute(sender, receiver, amountAfterTaxes) == TransactionResult.SUCCESS) {
            var bankBalanceManager = new BankBalanceManager(datastore, bank);
            if (bankBalanceManager.increaseBalance(amountDelta).success()) {
                return PurchaseResult.SUCCESS;
            }
        }
        return PurchaseResult.FAILURE;
    }
}
