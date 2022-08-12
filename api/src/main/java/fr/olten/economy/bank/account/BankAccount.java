package fr.olten.economy.bank.account;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.UUID;

@Entity(value = "bank_accounts", discriminator = "bank_account")
public class BankAccount {

    @Id
    private @Getter final ObjectId _id;
    private @Getter final ObjectId bankId;
    private @Getter final UUID owner;
    private @Getter final double minBalance, maxBalance, balance;

    public BankAccount(ObjectId _id, ObjectId bankId, UUID owner, double minBalance, double maxBalance, double balance) {
        this._id = _id;
        this.bankId = bankId;
        this.owner = owner;
        this.minBalance = minBalance;
        this.maxBalance = maxBalance;
        this.balance = balance;
    }
}
