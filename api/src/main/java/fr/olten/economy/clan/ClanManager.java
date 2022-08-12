package fr.olten.economy.clan;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.result.UpdateResult;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.olten.economy.clan.option.ClanOption;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

public class ClanManager {

    private final Datastore datastore;

    public ClanManager(Datastore datastore) {
        this.datastore = datastore;
    }

    public Clan getClan(String name){
        return this.datastore.find(Clan.class).filter(Filters.eq("name", name)).first();
    }

    public Query<Clan> queryClan(String name){
        return this.datastore.find(Clan.class).filter(Filters.eq("name", name));
    }

    public Clan getClan(ObjectId id){
        return this.datastore.find(Clan.class).filter(Filters.eq("_id", id)).first();
    }

    public Query<Clan> queryClan(ObjectId id){
        return this.datastore.find(Clan.class).filter(Filters.eq("_id", id));
    }

    public <T> void mergeClan(T entity){
        datastore.merge(entity);
    }

    public ImmutableList<Clan> getClans(){
        return ImmutableList.copyOf(this.datastore.find(Clan.class).stream().toList());
    }

    public Clan getClan(UUID uuid){
        var clans = this.datastore.find(Clan.class);
        if(clans.filter(Filters.eq("owner", uuid)).count() > 0){
            return clans.filter(Filters.eq("owner", uuid)).first();
        }
        return clans.filter(Filters.eq("players", uuid)).first();
    }

    /**
     * Join a clan.<br>
     * Note: Nothing will change if the player is already in the clan or the owner.
     * @param clan The clan to join.
     * @param uuid The player's uuid.
     * @return The update result.
     */
    public UpdateResult joinClan(Clan clan, UUID uuid){
        return queryClan(clan.getId())
                .filter(Filters.ne("owner", uuid),
                        Filters.nin("players", uuid),
                        Filters.lte("maxPlayers", clan.getPlayers().size()))
                .update(UpdateOperators.addToSet("players", uuid))
                .execute();
    }

    /**
     * Leave a clan if the player is in it.<br>
     * Warning: this method will not remove the player from the clan if he is the owner.
     * @param clan the clan to leave
     * @param uuid the player's uuid
     * @return the update result
     */
    public UpdateResult leaveClan(Clan clan, UUID uuid){
        return queryClan(clan.getId())
                .filter(Filters.nin("players", uuid))
                .update(UpdateOperators.pullAll("players", List.of(uuid)))
                .execute();
    }

    public void setOption(Clan clan, ClanOption option, Object value){
        if(!value.getClass().isInstance(option.getValueType())){
            throw new IllegalArgumentException("Incompatible value type. Should be a type of : " + option.getValueType().getSimpleName());
        }

        if (!clan.getOptions().containsKey(option))
            clan.getOptions().put(option, value);
        else
            clan.getOptions().replace(option, value);

        mergeClan(clan);
    }

    public void create(Clan clan){
        this.datastore.save(clan);
    }
}
