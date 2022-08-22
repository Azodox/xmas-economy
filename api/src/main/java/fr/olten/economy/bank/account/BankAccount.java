package fr.olten.economy.bank.account;

import dev.morphia.annotations.*;
import fr.olten.economy.bank.BalanceType;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.UUID;

@Entity(value = "bank_accounts", discriminator = "bank_account")
@Indexes({
        @Index(fields = @Field("owner"), options = @IndexOptions(unique = true)),
})
public class BankAccount {

    @Id
    private @Getter final ObjectId _id;
    private @Getter final ObjectId bankId;
    private @Getter final UUID owner;
    private @Getter final double minFlc, maxFlc, flc, minRs, maxRs, rs;

    public BankAccount(ObjectId _id, ObjectId bankId, UUID owner, double minFlc, double maxFlc, double flc,
                       double minRs, double maxRs, double rs) {
        this._id = _id;
        this.bankId = bankId;
        this.owner = owner;
        this.minFlc = minFlc;
        this.maxFlc = maxFlc;
        this.flc = flc;
        this.minRs = minRs;
        this.maxRs = maxRs;
        this.rs = rs;
    }

    public double getBalance(BalanceType type){
        return switch (type) {
            case FLC -> this.getFlc();
            case RS -> this.getRs();
        };
    }

    public double getMinBalance(BalanceType type){
        return switch (type) {
            case FLC -> this.getMinFlc();
            case RS -> this.getMinRs();
        };
    }

    public double getMaxBalance(BalanceType type){
        return switch (type) {
            case FLC -> this.getMaxFlc();
            case RS -> this.getMaxRs();
        };
    }
}
