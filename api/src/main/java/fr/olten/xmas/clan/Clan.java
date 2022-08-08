package fr.olten.xmas.clan;

import dev.morphia.annotations.*;
import fr.olten.xmas.clan.option.ClanOption;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Indexes({
        @Index(fields = @Field("name"), options = @IndexOptions(unique = true)),
        @Index(fields = @Field("displayName")),
        @Index(fields = @Field("color")),
        @Index(fields = @Field("owner"), options = @IndexOptions(unique = true)),
        @Index(fields = @Field("players"))
})
public class Clan {

    @Id
    private @Getter ObjectId id;
    private @Getter final String name;
    private final String displayName;
    private final String color;
    private @Getter final int maxPlayers;
    private @Getter final UUID owner;
    private @Getter final List<UUID> players;
    private @Getter final Map<ClanOption, Object> options;

    public Clan(String name, String displayName, String color, int maxPlayers, UUID owner, List<UUID> players, Map<ClanOption, Object> options) {
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.maxPlayers = maxPlayers;
        this.owner = owner;
        this.players = players;
        options.forEach((option, value) -> {
            if(!value.getClass().isInstance(option.getValueType())){
                throw new IllegalArgumentException("Option " + option.name() + " is not of type " + option.getValueType().getSimpleName());
            }
        });
        this.options = options;
    }

    public Component displayName(){
        return MiniMessage.builder().build().deserialize(this.displayName);
    }

    public TextColor color(){
        return TextColor.fromHexString(this.color);
    }
}
