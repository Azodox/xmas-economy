package fr.olten.xmas;

import co.aikar.commands.PaperCommandManager;
import dev.morphia.Datastore;
import fr.olten.xmas.clan.ClanManager;
import fr.olten.xmas.clan.option.ClanOption;
import fr.olten.xmas.commands.ClanCommand;
import fr.olten.xmas.commands.EconomyCommand;
import fr.olten.xmas.mongo.Mongo;
import fr.olten.xmas.mongo.MorphiaInitializer;
import lombok.Getter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Economy extends JavaPlugin {

    private @Getter Datastore datastore;
    private @Getter ClanManager clanManager;

    private @Getter final Map<UUID, ArmorStand> belowNameStands = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        var mongo = new Mongo(
                getConfig().getString("mongodb.username"),
                getConfig().getString("mongodb.authDatabase"),
                getConfig().getString("mongodb.password"),
                getConfig().getString("mongodb.host"),
                getConfig().getInt("mongodb.port")
        );
        this.datastore = new MorphiaInitializer(this.getClass(), mongo.getMongoClient(), "xmas", new String[]{"fr.olten.xmas.clan"}).getDatastore();
        this.clanManager = new ClanManager(this.datastore);

        var commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerCompletion("hex", c -> {
            if (c.getInput().length() > 7) {
                return List.of();
            }
            return List.of("#", "#FFFFFF", "#000000");
        });

        commandManager.getCommandCompletions().registerCompletion("option", c -> Arrays.stream(ClanOption.values()).map(ClanOption::name).toList());
        commandManager.getCommandContexts().registerContext(ClanOption.class, c -> {
            AtomicReference<ClanOption> option = new AtomicReference<>();
            c.getArgs().forEach(arg -> {
                if(Arrays.stream(ClanOption.values()).anyMatch(opt -> opt.name().equals(arg))) {
                    option.set(ClanOption.valueOf(arg));
                }
            });
            return option.get();
        });
        commandManager.getCommandContexts().registerContext(ClanOption[].class, c -> c.getArgs().stream().map(ClanOption::valueOf).toArray(ClanOption[]::new));
        commandManager.getCommandContexts().registerContext(Object.class, c -> {
            if(c.hasFlag("optionValue")){
                return c;
            }
            return null;
        });

        commandManager.registerCommand(new ClanCommand(this));
        commandManager.registerCommand(new EconomyCommand());

        getLogger().info("Enabled!");
    }
}
