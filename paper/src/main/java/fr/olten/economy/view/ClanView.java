package fr.olten.economy.view;

import fr.olten.economy.clan.Clan;
import fr.olten.economy.ladder.RankingLadder;
import fr.olten.economy.util.ItemBuilder;
import me.saiintbrisson.minecraft.View;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ClanView extends View {

    private final Clan clan;

    public ClanView(Clan clan) {
        super(5 * 9, "§6§lInformations §8❱ §r" + clan.getName());

        this.clan = clan;

        this.setCancelOnClick(true);
        this.setCancelOnClone(true);
        this.setCancelOnDrag(true);
        this.setCancelOnDrop(true);
        this.setCancelOnMoveOut(true);
        this.setCancelOnPickup(true);
        this.setCancelOnShiftClick(true);

        var fallbackItem = new ItemBuilder(Material.BEDROCK, 1).build();

        this.slot(2, 5, fallbackItem).onRender(render -> {
            if(this.clan.getOwner().equals(render.getPlayer().getUniqueId())){
                render.setItem(new ItemBuilder(Material.TOTEM_OF_UNDYING, 1)
                        .displayName("§e§lVous êtes le propriétaire du clan")
                        .build());
            }
        });

        this.slot(3, 3, fallbackItem).onUpdate(render -> render.withItem(new ItemBuilder(Material.BOOK, 1)
                .displayName("§b§lMembres")
                .lore(clan.getPlayers().stream().map(player -> "§7" + Bukkit.getOfflinePlayer(player).getName()).toList())
                .build()));
        this.slot(3, 5, fallbackItem).onUpdate(render -> render.withItem(new ItemBuilder(Material.PAPER, 1)
                .displayName("§6§lInformations")
                .lore(
                        "§7Nom : " + clan.getName(),
                        "§7DisplayName : " + MiniMessage.miniMessage().serialize(clan.displayName()),
                        "§7Couleur : " + clan.color(),
                        "§7Propriétaire : " + Bukkit.getOfflinePlayer(clan.getOwner()).getName()
                )
                .build()));
        this.slot(3, 7, fallbackItem).onUpdate(render -> render.withItem(new ItemBuilder(Material.LADDER, 1)
                .displayName("§a§lClassement : #" + RankingLadder.getRanking(clan))
                .build()));
        this.slot(5, 4, fallbackItem).onUpdate(update -> {
            var builder = new ItemBuilder(Material.REDSTONE, 1).displayName("§c§lStatus des options");
            builder.lore("§8§m                     ");
            clan.getOptions().forEach((option, value) -> builder.lore("§7" + option.name().toLowerCase() + " : §b" + value));
            builder.lore("§8§m                     ");
            update.withItem(builder.build());
        });
        this.slot(5, 5, new ItemBuilder(Material.LEGACY_REDSTONE_COMPARATOR, 1).displayName("§c§lGérer les options de clan").build());
        this.scheduleUpdate(20);
    }
}
