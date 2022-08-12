package fr.olten.economy.bank;

import dev.morphia.annotations.*;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.Map;
import java.util.UUID;

@Entity(value = "banks", discriminator = "bank")
@Indexes({
        @Index(fields = @Field("name"), options = @IndexOptions(unique = true)),
})
public class Bank {

    @Id
    private @Getter final ObjectId _id;
    private @Getter final String name, displayName;
    private @Getter final double balance;
    private @Getter final Map<UUID, ObjectId> accounts;

    public Bank(ObjectId _id, String name, String displayName, double balance, Map<UUID, ObjectId> accounts) {
        this._id = _id;
        this.name = name;
        this.displayName = displayName;
        this.balance = balance;
        this.accounts = accounts;
    }
}
