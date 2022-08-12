package fr.olten.economy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.olten.economy.Economy;
import fr.olten.economy.clan.Clan;
import fr.olten.economy.clan.option.ClanOption;
import fr.olten.economy.view.ClanView;
import me.saiintbrisson.minecraft.ViewFrame;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

@CommandAlias("clan")
public class ClanCommand extends BaseCommand {

    private final Economy economy;

    public ClanCommand(Economy economy) {
        this.economy = economy;
    }

    @Default
    public void clan(Player player){
        var clan = this.economy.getClanManager().getClan(player.getUniqueId());
        if(clan == null){
            player.sendMessage(Component.text("Vous n'avez pas de clan.").color(NamedTextColor.RED));
            return;
        }

        var frame = ViewFrame.of(economy, new ClanView(clan)).register();
        frame.open(ClanView.class, player);
    }

    @Subcommand("list")
    public void clanList(Player player){
        player.sendMessage(Component.join(JoinConfiguration.commas(true),
                economy.getClanManager().getClans().stream().map(clan ->
                        clan.displayName().hoverEvent(HoverEvent.showText(Component.text(
                                "Clan: " + clan.getName() + "\n" +
                                        "DisplayName: " + MiniMessage.miniMessage().serialize(clan.displayName()) + "\n" +
                                        "Color: " + clan.color() + "\n")))
                ).toList()));
    }

    @Subcommand("create")
    @Syntax("<name> <displayName> <color>")
    @CommandCompletion("<name> <displayName> <color>|@hex")
    public void create(Player player, String name, String displayName, String color){
        economy.getClanManager().create(new Clan(name, displayName, color, 2, player.getUniqueId(), List.of(player.getUniqueId()), Map.of(ClanOption.FRIENDLY_FIRE, false)));
        player.sendMessage(Component.text("Clan " + name + " créé"));
    }

    @Subcommand("modify name")
    @Syntax("<name> <newName>")
    @CommandCompletion("<name> <newName>")
    public void modifyName(CommandSender sender, String name, String newName){
        var clan = economy.getClanManager().queryClan(name);
        if(clan.count() == 0){
            sender.sendMessage(Component.text("Clan " + name + " introuvable"));
            return;
        }

        if (clan.update(UpdateOperators.set("name", newName)).execute().getModifiedCount() > 0)
            sender.sendMessage(Component.text("Clan " + name + " renommé en " + newName));
        else
            sender.sendMessage(Component.text("Impossible de renommer le clan " + name));
    }

    @Subcommand("set")
    @Syntax("<name> <option> <value>")
    @CommandCompletion("<name> @option <value>")
    public void setOption(CommandSender sender, String name, ClanOption option, Boolean value){
        var queryClan = economy.getClanManager().queryClan(name);
        var clan = economy.getClanManager().getClan(name);
        if(queryClan.count() == 0){
            sender.sendMessage(Component.text("Clan " + name + " introuvable"));
            return;
        }

        economy.getClanManager().setOption(clan, option, value);
        sender.sendMessage(Component.text("Option \"" + option.name() + "\" du clan <" + name + "> " + "mis à " + value));
    }

    @HelpCommand
    public void onHelp(Player player){
        player.sendMessage(Component.text("/clan list|create <name> <displayName> <color>").color(NamedTextColor.RED));
    }
}
